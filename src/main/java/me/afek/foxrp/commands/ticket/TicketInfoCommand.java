package me.afek.foxrp.commands.ticket;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.database.Sql;
import me.afek.foxrp.objects.TicketData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketInfoCommand implements CommandExecutor {

    Sql sql;
    DataCommon dataCommon;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("foxrp.commands.ticketinfo")) {
            sender.sendMessage(StringCommon.color(Settings.IMP.PERMISSION_MSG));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_EDIT_COMMAND.USE));
            return true;
        }

        String ticketId = args[0];
        if (!this.dataCommon.containTicket(ticketId)) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_EDIT_COMMAND.NOT_EXIST));
            return true;
        }

        TicketData ticketData = this.dataCommon.getTicket(ticketId);
        sender.sendMessage(StringCommon.color("&a__________ " + ticketData.getIdTicket() + " __________"));
        sender.sendMessage(StringCommon.color("&aPlayer name: &6" + ticketData.getName()));
        sender.sendMessage(StringCommon.color("&aDiamonds: &6" + ticketData.getDiamonds()));
        sender.sendMessage(StringCommon.color("&aLeft time: &6" + StringCommon.formatCountdownTime((ticketData.getFinalTime() - System.currentTimeMillis()) / 1000L)));
        sender.sendMessage(StringCommon.color("&aReason: &6" + ticketData.getReason()));
        return true;
    }
}
