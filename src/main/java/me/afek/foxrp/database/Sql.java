package me.afek.foxrp.database;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import me.afek.foxrp.FoxRPPlugin;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.objects.TicketData;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Sql {

    FoxRPPlugin plugin;
    DataCommon dataCommon;
    ExecutorService executor = Executors.newFixedThreadPool(2, new ThreadFactoryBuilder().setNameFormat("DailyRewards-SQL-%d").build());
    Logger logger = Bukkit.getLogger();

    @NonFinal
    Connection connection;
    @NonFinal
    boolean connecting = false;

    public Sql(FoxRPPlugin plugin, DataCommon dataCommon) {
        this.plugin = plugin;
        this.dataCommon = dataCommon;
        setupConnect();
    }

    private void setupConnect() {

        try {
            connecting = true;

            if (executor.isShutdown() || (connection != null && connection.isValid(3)))
                return;

            logger.info("[FoxRP] Connect to database...");
            long start = System.currentTimeMillis();
            if (Settings.IMP.SQL.STORAGE_TYPE.equalsIgnoreCase("mysql")) {
                Settings.SQL s = Settings.IMP.SQL;
                connectToDatabase(String.format("JDBC:mysql://%s:%s/%s?useSSL=false&useUnicode=true&characterEncoding=utf-8", s.HOSTNAME, String.valueOf(s.PORT), s.DATABASE), s.USER, s.PASSWORD);
            } else {
                Class.forName("org.sqlite.JDBC");
                connectToDatabase("JDBC:sqlite:" + this.plugin.getDataFolder() + "/database.db", null, null);
            }
            logger.log(Level.INFO, "[FoxRP] Connected ({0} ms)", System.currentTimeMillis() - start);
            createTable();
            loadTickets();
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.WARNING, "Can not connect to database or execute sql: ", e);
            connection = null;
        } finally {
            connecting = false;
        }
    }

    private void connectToDatabase(String url, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
    }

    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS `Tickets` ("
                + "`Ticket` VARCHAR(16) NOT NULL PRIMARY KEY UNIQUE,"
                + "`Player` VARCHAR(16) NOT NULL,"
                + "`Diamonds` INT(10) DEFAULT 0,"
                + "`FinalTime` BIGINT NOT NULL,"
                + "`Reason` TEXT);";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }

    private void loadTickets() throws SQLException {
        try (PreparedStatement statament = connection.prepareStatement("SELECT * FROM `Tickets`;");
             ResultSet set = statament.executeQuery()) {
            int i = 0;
            while (set.next()) {
                String ticket = set.getString("Ticket");
                String player = set.getString("Player");
                int diamonds = set.getInt("Diamonds");
                long finalTime = set.getLong("FinalTime");
                String reason = set.getString("Reason");

                if (this.isInvalidName(player)) {
                    this.removeTicket(player);
                    continue;
                }

                this.dataCommon.addTicket(new TicketData(ticket, player, reason, diamonds, finalTime));
                i++;
            }

            logger.log(Level.INFO, "[FoxRP] Successfully load tickets from database ({0})", i);
        }
    }

    private boolean isInvalidName(String name) {
        return name.contains("'") || name.contains("\"");
    }

    private void removeTicket(String ticketId) {
        String sql = "REMOVE FROM `Tickets` WHERE `Ticket` = '" + ticketId.toLowerCase() + "';";
        this.removeTicketSql(sql);
    }

    private void removeTicketSql(String sql) {
        if (connection != null) {
            this.executor.execute(() ->
            {
                try (PreparedStatement statament = connection.prepareStatement(sql)) {
                    statament.execute();
                } catch (SQLException ignored) {
                }
            });
        }
    }

    public void saveTicket(TicketData ticketData) {
        if (connecting)
            return;

        if (connection != null) {
            this.executor.execute(() -> {
                final long timestamp = System.currentTimeMillis();
                String sql = "SELECT `Ticket` FROM `Tickets` where `Ticket` = '" + ticketData.getIdTicket().toLowerCase() + "' LIMIT 1;";
                try (Statement statament = connection.createStatement();
                     ResultSet set = statament.executeQuery(sql)) {
                    if (!set.next()) {
                        sql = "INSERT INTO `Tickets` (`Ticket`, `Player`, `Diamonds`, `FinalTime`, `Reason`) VALUES ('" + ticketData.getIdTicket().toLowerCase() + "','" + ticketData.getName().toLowerCase() + "',"
                                + "'" + ticketData.getDiamonds() + "','" + ticketData.getFinalTime() + "','" + ticketData.getReason() + "');";
                        statament.executeUpdate(sql);
                    } else {
                        sql = "UPDATE `Tickets` SET `Player` = '" + ticketData.getName().toLowerCase() + "', `Diamonds` = '" + ticketData.getDiamonds() + "', `FinalTime` = '" + ticketData.getFinalTime() + "', `Reason` = '" + ticketData.getReason() + "'"
                                + " where `Ticket` = '" + ticketData.getIdTicket().toLowerCase() + "';";
                        statament.executeUpdate(sql);
                    }
                } catch (SQLException ex) {
                    logger.log(Level.WARNING, "[DailyRewards] Can't query database", ex);
                    logger.log(Level.WARNING, sql);
                    executor.execute(this::setupConnect);
                }
            });
        }
    }

    public void close() {
        this.executor.shutdownNow();
        try {
            if (connection != null)
                this.connection.close();
        } catch (SQLException ignore) {
        }
        this.connection = null;
    }
}