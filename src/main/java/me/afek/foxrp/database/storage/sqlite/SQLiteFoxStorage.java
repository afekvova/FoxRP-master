package me.afek.foxrp.database.storage.sqlite;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import me.afek.foxrp.FoxRPPlugin;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.database.FoxStorage;
import me.afek.foxrp.model.Ticket;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SQLiteFoxStorage implements FoxStorage {

    FoxRPPlugin plugin;

    ExecutorService executor = Executors.newFixedThreadPool(2, new ThreadFactoryBuilder().setNameFormat("FoxRP-SQL-%d").build());
    Logger logger = Bukkit.getLogger();

    @NonFinal
    Connection connection;

    @Getter
    @NonFinal
    boolean connected = false;

    //TODO: Перевести всё на библиотеку ormlite
    public SQLiteFoxStorage(FoxRPPlugin plugin) {
        this.plugin = plugin;

        this.connect();
    }

    @Override
    public boolean connect() {
        try {
            this.connected = true;

            if (this.executor.isShutdown() || (this.connection != null && this.connection.isValid(3))) return false;

            this.logger.info("[FoxRP] Connect to database...");

            long start = System.currentTimeMillis();
            if (Settings.IMP.SQL.STORAGE_TYPE.equalsIgnoreCase("mysql")) {
                Settings.SQL s = Settings.IMP.SQL;
                connectToDatabase(String.format("JDBC:mysql://%s:%s/%s?useSSL=false&useUnicode=true&characterEncoding=utf-8", s.HOSTNAME, s.PORT, s.DATABASE), s.USER, s.PASSWORD);
            } else {
                Class.forName("org.sqlite.JDBC");
                connectToDatabase("JDBC:sqlite:" + this.plugin.getDataFolder() + "/database.db", null, null);
            }

            this.logger.log(Level.INFO, "[FoxRP] Connected ({0} ms)", System.currentTimeMillis() - start);

            createTable();
            loadTickets();
            loadWarnings();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            this.logger.log(Level.WARNING, "Can not connect to database or execute sql: ", e);
            connection = null;
        } finally {
            connected = false;
        }

        return false;
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

        sql = "CREATE TABLE IF NOT EXISTS `Warnings` ("
                + "`PlayerName` VARCHAR(16) NOT NULL PRIMARY KEY UNIQUE,"
                + "`Warns` INT(10) DEFAULT 0);";

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

                plugin.getTicketRepository().addData(ticket, new Ticket(ticket, player, reason, diamonds, finalTime));
                i++;
            }

            logger.log(Level.INFO, "[FoxRP] Successfully load tickets from database ({0})", i);
        }
    }

    private void loadWarnings() throws SQLException {
        try (PreparedStatement statament = connection.prepareStatement("SELECT * FROM `Warnings`;");
             ResultSet set = statament.executeQuery()) {
            int i = 0;
            while (set.next()) {
                String playerName = set.getString("PlayerName");
                int warnings = set.getInt("Warns");

                if (this.isInvalidName(playerName)) {
                    this.removeTicket(playerName);
                    continue;
                }

                plugin.getWarningRepository().addData(playerName, warnings);
                i++;
            }

            logger.log(Level.INFO, "[FoxRP] Successfully load warnings from database ({0})", i);
        }
    }

    @Override
    public void saveTicket(Ticket ticketData) {
        if (connected)
            return;

        if (connection != null) {
            this.executor.execute(() -> {
                final long timestamp = System.currentTimeMillis();
                String sql = "SELECT `Ticket` FROM `Tickets` where `Ticket` = '" + ticketData.getIdTicket() + "' LIMIT 1;";
                try (Statement statament = connection.createStatement();
                     ResultSet set = statament.executeQuery(sql)) {
                    if (!set.next()) {
                        sql = "INSERT INTO `Tickets` (`Ticket`, `Player`, `Diamonds`, `FinalTime`, `Reason`) VALUES ('" + ticketData.getIdTicket() + "','" + ticketData.getName().toLowerCase() + "',"
                                + "'" + ticketData.getDiamonds() + "','" + ticketData.getFinalTime() + "','" + ticketData.getReason() + "');";
                        statament.executeUpdate(sql);
                    } else {
                        sql = "UPDATE `Tickets` SET `Player` = '" + ticketData.getName().toLowerCase() + "', `Diamonds` = '" + ticketData.getDiamonds() + "', `FinalTime` = '" + ticketData.getFinalTime() + "', `Reason` = '" + ticketData.getReason() + "'"
                                + " where `Ticket` = '" + ticketData.getIdTicket() + "';";
                        statament.executeUpdate(sql);
                    }
                } catch (SQLException ex) {
                    logger.log(Level.WARNING, "[FoxRP] Can't query database", ex);
                    logger.log(Level.WARNING, sql);
                    executor.execute(this::connect);
                }
            });
        }
    }

    @Override
    public void saveWarning(String playerName, int warnings) {
        if (connected || isInvalidName(playerName))
            return;

        if (connection != null) {
            this.executor.execute(() -> {
                String sql = "SELECT `PlayerName` FROM `Warnings` where `PlayerName` = '" + playerName.toLowerCase() + "' LIMIT 1;";
                try (Statement statament = connection.createStatement();
                     ResultSet set = statament.executeQuery(sql)) {
                    if (!set.next()) {
                        sql = "INSERT INTO `Warnings` (`PlayerName`, `Warns`) VALUES ('" + playerName.toLowerCase() + "','" + warnings + "');";
                        statament.executeUpdate(sql);
                    } else {
                        sql = "UPDATE `Warnings` SET `Warns` = '" + warnings + "' where `PlayerName` = '" + playerName.toLowerCase() + "';";
                        statament.executeUpdate(sql);
                    }
                } catch (SQLException ex) {
                    logger.log(Level.WARNING, "[FoxRP] Can't query database", ex);
                    logger.log(Level.WARNING, sql);
                    executor.execute(this::connect);
                }
            });
        }
    }

    private boolean isInvalidName(String name) {
        return name.contains("'") || name.contains("\"");
    }

    @Override
    public void removeWarning(String playerName) {
        String sql = "DELETE FROM `Warnings` WHERE `PlayerName` = '" + playerName.toLowerCase() + "';";
        this.execute(sql);
    }

    @Override
    public void removeTicket(String ticketId) {
        String sql = "DELETE FROM `Tickets` WHERE `Ticket` = '" + ticketId + "';";
        this.execute(sql);
    }

    private void execute(String sql) {
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

    @Override
    public void disconnect() {
        this.executor.shutdownNow();

        try {
            if (this.connection != null) this.connection.close();
        } catch (SQLException ignore) {}

        this.connection = null;
    }

}