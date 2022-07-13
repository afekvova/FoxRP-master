package me.afek.foxrp.commands.subcommands;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.commands.SubCommand;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.model.Ticket;
import org.bukkit.command.CommandSender;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketInfoCommand extends SubCommand {

    DataCommon dataCommon;

    public TicketInfoCommand(DataCommon dataCommon) {
        super("info", 1, Settings.IMP.TICKET_INFO_COMMAND.USE);
        this.dataCommon = dataCommon;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String ticketId = args[0];
        if (!this.dataCommon.containTicket(ticketId)) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_INFO_COMMAND.NOT_EXIST));
            return;
        }

        Ticket ticketData = this.dataCommon.getTicket(ticketId);
        Settings.IMP.TICKET_INFO_COMMAND.MESSAGE.forEach(message -> sender.sendMessage(StringCommon.color(message.replace("%ticketId%", ticketData.getIdTicket()).replace("%player%", ticketData.getName()).replace("%diamonds%", String.valueOf(ticketData.getDiamonds())).replace("%leftTime%", StringCommon.formatCountdownTime((ticketData.getFinalTime() - System.currentTimeMillis()) / 1000L)).replace("%reason%", ticketData.getReason()))));
    }
}
