package me.afek.foxrp.commands.ticket;

import com.google.common.base.Joiner;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.database.storage.sqlite.SQLiteFoxStorage;
import me.afek.foxrp.model.Ticket;
import me.afek.foxrp.utils.RandomString;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketGiveCommand implements CommandExecutor {

    SQLiteFoxStorage sql;
    DataCommon dataCommon;
    RandomString randomString = new RandomString(Settings.IMP.TICKET_GIVE_COMMAND.TICKET_ID_LENGTH);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("foxrp.commands.ticketgive")) {
            sender.sendMessage(StringCommon.color(Settings.IMP.PERMISSION_MSG));
            return true;
        }

        if (args.length < 4 || !StringCommon.isStringInt(args[1]) || !StringCommon.isStringInt(args[2])) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_GIVE_COMMAND.USE));
            return true;
        }

        String playerName = args[0];
        int diamonds = Integer.parseInt(args[1]);
        int hours = Integer.parseInt(args[2]);

        if (hours > Settings.IMP.TICKET_GIVE_COMMAND.MAX_TIME_TICKET || hours < Settings.IMP.TICKET_GIVE_COMMAND.MIN_TIME_TICKET) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_GIVE_COMMAND.TIME_ERROR));
            return true;
        }

        long hoursStamp = System.currentTimeMillis() + hours * (1000L * 3600L);
        String reason = Joiner.on(' ').join(Arrays.asList(Arrays.copyOfRange(args, 3, args.length)));

        Ticket ticketData = new Ticket("#" + this.randomString.nextString(), playerName, reason, diamonds, hoursStamp);
        this.dataCommon.addTicket(ticketData);
        this.sql.saveTicket(ticketData);

        sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_GIVE_COMMAND.SUCCESS.replace("%ticketId%", ticketData.getIdTicket()).replace("%player%", ticketData.getName())));

        Player player = Bukkit.getPlayer(playerName);
        if (player == null || !player.isOnline() || !Settings.IMP.TICKET_GIVE_COMMAND.SEND_PLAYER_MESSAGE) return true;
        player.sendMessage(StringCommon.color(Settings.IMP.TICKET_GIVE_COMMAND.PLAYER_MESSAGE.replace("%ticketId%", ticketData.getIdTicket())));
        return true;
    }
}
