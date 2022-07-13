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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketEditCommand implements CommandExecutor {

    SQLiteFoxStorage sql;
    DataCommon dataCommon;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("foxrp.commands.ticketedit")) {
            sender.sendMessage(StringCommon.color(Settings.IMP.PERMISSION_MSG));
            return true;
        }

        if (args.length < 5 || !StringCommon.isStringInt(args[2]) || !StringCommon.isStringInt(args[3])) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_EDIT_COMMAND.USE));
            return true;
        }

        String ticketId = args[0];
        if (!this.dataCommon.containTicket(ticketId)) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_EDIT_COMMAND.NOT_EXIST));
            return true;
        }

        String playerName = args[1];
        int diamonds = Integer.parseInt(args[2]);
        int hours = Integer.parseInt(args[2]);

        if (hours > Settings.IMP.TICKET_GIVE_COMMAND.MAX_TIME_TICKET || hours < Settings.IMP.TICKET_GIVE_COMMAND.MIN_TIME_TICKET) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_GIVE_COMMAND.TIME_ERROR));
            return true;
        }

        long hoursStamp = System.currentTimeMillis() + hours * (1000L * 3600L);
        String reason = Joiner.on(' ').join(Arrays.asList(Arrays.copyOfRange(args, 4, args.length)));

        Ticket ticketData = new Ticket(ticketId, playerName, reason, diamonds, hoursStamp);
        this.dataCommon.addTicket(ticketData);
        this.sql.saveTicket(ticketData);

        sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_EDIT_COMMAND.SUCCESS.replace("%ticketId%", ticketData.getIdTicket()).replace("%player%", ticketData.getName())));
        return true;
    }
}
