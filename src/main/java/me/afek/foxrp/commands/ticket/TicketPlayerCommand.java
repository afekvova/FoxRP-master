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

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketPlayerCommand implements CommandExecutor {

    Sql sql;
    DataCommon dataCommon;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("foxrp.commands.ticketinfo")) {
            sender.sendMessage(StringCommon.color(Settings.IMP.PERMISSION_MSG));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_EDIT_COMMAND.USE));
            return true;
        }

        String playerName = args[0];
        if (!this.dataCommon.containTicketByPlayer(playerName)) return true;

        int page = 1;
        if (args.length >= 2 && StringCommon.isStringInt(args[1]))
            page = Integer.parseInt(args[1]);

        List<TicketData> tickets = this.dataCommon.getTicketsByPlayer(playerName);

        int pageCount = (int) Math.ceil(tickets.size() / (double) 5);
        if (page < 1 || page > pageCount) {
            sender.sendMessage("page");
            return true;
        }

        final int lastComp = Math.min(page * 5, tickets.size());
        for (int i = (page - 1) * 5; i < lastComp; i++) {
            TicketData ticketData = tickets.get(i);
            if (ticketData == null) continue;

            sender.sendMessage(ticketData.getIdTicket() + ": " + ticketData.getReason());
        }

        return true;
    }
}
