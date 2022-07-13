package me.afek.foxrp.commands.subcommands;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.commands.SubCommand;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.model.Ticket;
import me.afek.foxrp.repositories.impl.TicketRepository;
import org.bukkit.command.CommandSender;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketInfoCommand extends SubCommand {
    TicketRepository repository;

    public TicketInfoCommand(TicketRepository repository) {
        super("info", 1, Settings.IMP.TICKET_INFO_COMMAND.USE);
        this.repository = repository;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String id = args[0];
        if (!this.repository.containData(id)) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_INFO_COMMAND.NOT_EXIST));
            return;
        }

        Ticket ticketData = this.repository.getData(id);
        Settings.IMP.TICKET_INFO_COMMAND.MESSAGE.forEach(message -> sender.sendMessage(StringCommon.color(message.replace("%ticketId%", ticketData.getIdTicket()).replace("%player%", ticketData.getName()).replace("%diamonds%", String.valueOf(ticketData.getDiamonds())).replace("%leftTime%", StringCommon.formatCountdownTime((ticketData.getFinalTime() - System.currentTimeMillis()) / 1000L)).replace("%reason%", ticketData.getReason()))));
    }
}
