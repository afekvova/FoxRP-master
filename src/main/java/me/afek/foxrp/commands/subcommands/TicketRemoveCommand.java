package me.afek.foxrp.commands.subcommands;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.commands.SubCommand;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.database.FoxStorage;
import me.afek.foxrp.model.Ticket;
import org.bukkit.command.CommandSender;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketRemoveCommand extends SubCommand {

    FoxStorage foxStorage;
    DataCommon dataCommon;

    public TicketRemoveCommand(FoxStorage foxStorage, DataCommon dataCommon) {
        super("remove", 1, Settings.IMP.TICKET_REMOVE_COMMAND.USE);
        this.foxStorage = foxStorage;
        this.dataCommon = dataCommon;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String ticketId = args[0];
        if (!this.dataCommon.containTicket(ticketId)) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_REMOVE_COMMAND.NOT_EXIST));
            return;
        }

        Ticket ticket = this.dataCommon.removeTicket(ticketId);
        this.foxStorage.removeTicket(ticket.getIdTicket());

        sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_REMOVE_COMMAND.SUCCESS.replace("%ticketId%", ticket.getIdTicket()).replace("%player%", ticket.getName())));
    }
}
