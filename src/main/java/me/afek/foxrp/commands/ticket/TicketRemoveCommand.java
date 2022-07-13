package me.afek.foxrp.commands.ticket;

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

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketRemoveCommand implements CommandExecutor {

    SQLiteFoxStorage sql;
    DataCommon dataCommon;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("foxrp.commands.ticketremove")) {
            sender.sendMessage(StringCommon.color(Settings.IMP.PERMISSION_MSG));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_REMOVE_COMMAND.USE));
            return true;
        }

        String ticketId = args[0];
        if (!this.dataCommon.containTicket(ticketId)) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_REMOVE_COMMAND.NOT_EXIST));
            return true;
        }

        Ticket ticketData = this.dataCommon.removeTicket(ticketId);
        this.sql.removeTicket(ticketData.getIdTicket());

        sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_REMOVE_COMMAND.SUCCESS.replace("%ticketId%", ticketData.getIdTicket()).replace("%player%", ticketData.getName())));
        return true;
    }
}
