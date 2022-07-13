package me.afek.foxrp.commands.subcommands;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.commands.SubCommand;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.model.Ticket;
import me.afek.foxrp.repositories.impl.TicketRepository;
import me.afek.foxrp.repositories.impl.WarningRepository;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketPlayerCommand extends SubCommand {

    TicketRepository ticketRepository;
    WarningRepository warningRepository;

    public TicketPlayerCommand(TicketRepository ticketRepository, WarningRepository warningRepository) {
        super("player", 1, Settings.IMP.TICKET_PLAYER_COMMAND.USE);
        this.ticketRepository = ticketRepository;
        this.warningRepository = warningRepository;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String playerName = args[0];
        if (!this.containTicketByPlayer(playerName)) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_PLAYER_COMMAND.NOT_EXIST));
            return;
        }

        int page = 1;
        if (args.length >= 2 && StringCommon.isStringInt(args[1]))
            page = Integer.parseInt(args[1]);

        List<Ticket> tickets = this.getTicketsByPlayer(playerName);

        int pageCount = (int) Math.ceil(tickets.size() / (double) 5);
        if (page < 1 || page > pageCount) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_PLAYER_COMMAND.USE));
            return;
        }

        sender.sendMessage(StringCommon.color("Игрок: &6" + playerName));
        sender.sendMessage(StringCommon.color("Всего штрафов: &6" + tickets.size()));
        sender.sendMessage(StringCommon.color("Варнов: &6" + this.warningRepository.getData(playerName)));

        // TODO: Может использовать com.google.common.collect.Lists#partition?
        int lastComp = Math.min(page * 5, tickets.size());
        for (int i = (page - 1) * 5; i < lastComp; i++) {
            Ticket ticket = tickets.get(i);
            if (ticket != null)
                sender.sendMessage((i + 1) + ") " + ticket.getIdTicket() + ": " + ticket.getReason());
        }
    }

    private boolean containTicketByPlayer(String playerName) {
        for (Ticket ticket : this.ticketRepository.getData())
            if (ticket.getName().equalsIgnoreCase(playerName.toLowerCase())) return true;

        return false;
    }

    public List<Ticket> getTicketsByPlayer(String playerName) {
        List<Ticket> tickets = new ArrayList<>();

        for (Ticket ticket : this.ticketRepository.getData())
            if (ticket.getName().equalsIgnoreCase(playerName.toLowerCase()))
                tickets.add(ticket);

        return tickets;
    }
}
