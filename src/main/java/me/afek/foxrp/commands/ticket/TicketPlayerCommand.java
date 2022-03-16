package me.afek.foxrp.commands.ticket;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.objects.TicketData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketPlayerCommand implements CommandExecutor {

    DataCommon dataCommon;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("foxrp.commands.ticketplayer")) {
            sender.sendMessage(StringCommon.color(Settings.IMP.PERMISSION_MSG));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_PLAYER_COMMAND.USE));
            return true;
        }

        String playerName = args[0];
        if (!this.dataCommon.containTicketByPlayer(playerName)) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_PLAYER_COMMAND.NOT_EXIST));
            return true;
        }

        int page = 1;
        if (args.length >= 2 && StringCommon.isStringInt(args[1]))
            page = Integer.parseInt(args[1]);

        List<TicketData> tickets = this.dataCommon.getTicketsByPlayer(playerName);

        int pageCount = (int) Math.ceil(tickets.size() / (double) 5);
        if (page < 1 || page > pageCount) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_PLAYER_COMMAND.USE));
            return true;
        }

        sender.sendMessage(StringCommon.color("%prefix%Игрок: &6" + playerName));
        sender.sendMessage(StringCommon.color("Всего штрафов: &6" + tickets.size()));
        sender.sendMessage(StringCommon.color("Варнов: &6" + this.dataCommon.getPlayerWarnings(playerName)));

        final int lastComp = Math.min(page * 5, tickets.size());
        for (int i = (page - 1) * 5; i < lastComp; i++) {
            TicketData ticketData = tickets.get(i);
            if (ticketData == null) continue;

            sender.sendMessage((i + 1) + ") " + ticketData.getIdTicket() + ": " + ticketData.getReason());
        }

        return true;
    }
}
