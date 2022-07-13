package me.afek.foxrp.commands.subcommands;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.commands.SubCommand;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.model.Ticket;
import org.bukkit.command.CommandSender;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketPlayerCommand extends SubCommand {

    DataCommon dataCommon;

    public TicketPlayerCommand(DataCommon dataCommon) {
        super("player", 1, Settings.IMP.TICKET_PLAYER_COMMAND.USE);
        this.dataCommon = dataCommon;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String playerName = args[0];
        if (!this.dataCommon.containTicketByPlayer(playerName)) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_PLAYER_COMMAND.NOT_EXIST));
            return;
        }

        int page = 1;
        if (args.length >= 2 && StringCommon.isStringInt(args[1]))
            page = Integer.parseInt(args[1]);

        List<Ticket> tickets = this.dataCommon.getTicketsByPlayer(playerName);

        int pageCount = (int) Math.ceil(tickets.size() / (double) 5);
        if (page < 1 || page > pageCount) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_PLAYER_COMMAND.USE));
            return;
        }

        sender.sendMessage(StringCommon.color("%prefix%Игрок: &6" + playerName));
        sender.sendMessage(StringCommon.color("Всего штрафов: &6" + tickets.size()));
        sender.sendMessage(StringCommon.color("Варнов: &6" + this.dataCommon.getPlayerWarnings(playerName)));

        int lastComp = Math.min(page * 5, tickets.size());
        for (int i = (page - 1) * 5; i < lastComp; i++) {
            Ticket ticket = tickets.get(i);
            if (ticket != null)
                sender.sendMessage((i + 1) + ") " + ticket.getIdTicket() + ": " + ticket.getReason());
        }
    }
}
