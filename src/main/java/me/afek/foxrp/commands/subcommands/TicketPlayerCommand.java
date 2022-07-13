package me.afek.foxrp.commands.subcommands;

import com.google.common.collect.Lists;
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

        int page = 0;
        if (args.length >= 2 && StringCommon.isStringInt(args[1]))
            page = (Integer.parseInt(args[1]) - 1);

        List<List<Ticket>> tickets = Lists.partition(this.getTicketsByPlayer(playerName), 5);

        if (page < 0 || page > tickets.size() - 1) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_PLAYER_COMMAND.USE));
            return;
        }

        sender.sendMessage(StringCommon.color("Игрок: &6" + playerName));
        sender.sendMessage(StringCommon.color("Всего штрафов: &6" + tickets.size()));
        sender.sendMessage(StringCommon.color("Варнов: &6" + this.warningRepository.getData(playerName)));


        int index = 5 * page;
        for (Ticket ticket : tickets.get(page))
            sender.sendMessage((++index) + ") " + ticket.getIdTicket() + ": " + ticket.getReason());
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
