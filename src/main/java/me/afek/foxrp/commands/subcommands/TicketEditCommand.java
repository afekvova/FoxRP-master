package me.afek.foxrp.commands.subcommands;

import com.google.common.base.Joiner;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.commands.SubCommand;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.database.FoxStorage;
import me.afek.foxrp.model.Ticket;
import me.afek.foxrp.repositories.impl.TicketRepository;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketEditCommand extends SubCommand {

    FoxStorage foxStorage;
    TicketRepository repository;

    public TicketEditCommand(FoxStorage foxStorage, TicketRepository repository) {
        super("edit", 5, Settings.IMP.TICKET_EDIT_COMMAND.USE);
        this.foxStorage = foxStorage;
        this.repository = repository;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 5 || !StringCommon.isStringInt(args[2]) || !StringCommon.isStringInt(args[3])) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_EDIT_COMMAND.USE));
            return;
        }

        String id = args[0];
        if (!this.repository.containData(id)) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_EDIT_COMMAND.NOT_EXIST));
            return;
        }

        String playerName = args[1];
        int diamonds = Integer.parseInt(args[2]);
        int hours = Integer.parseInt(args[3]);

        if (hours > Settings.IMP.TICKET_GIVE_COMMAND.MAX_TIME_TICKET || hours < Settings.IMP.TICKET_GIVE_COMMAND.MIN_TIME_TICKET) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_GIVE_COMMAND.TIME_ERROR));
            return;
        }

        long hoursStamp = System.currentTimeMillis() + hours * (1000L * 3600L);
        String reason = Joiner.on(' ').join(Arrays.asList(Arrays.copyOfRange(args, 4, args.length)));

        Ticket ticket = new Ticket(id, playerName, reason, diamonds, hoursStamp);
        this.repository.addData(id, ticket);
        this.foxStorage.saveTicket(ticket);

        sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_EDIT_COMMAND.SUCCESS.replace("%id%", ticket.getIdTicket()).replace("%player%", ticket.getName())));
    }
}
