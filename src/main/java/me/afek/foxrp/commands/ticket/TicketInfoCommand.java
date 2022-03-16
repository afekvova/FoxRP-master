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

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketInfoCommand implements CommandExecutor {

    DataCommon dataCommon;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("foxrp.commands.ticketinfo")) {
            sender.sendMessage(StringCommon.color(Settings.IMP.PERMISSION_MSG));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_INFO_COMMAND.USE));
            return true;
        }

        String ticketId = args[0];
        if (!this.dataCommon.containTicket(ticketId)) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_INFO_COMMAND.NOT_EXIST));
            return true;
        }

        TicketData ticketData = this.dataCommon.getTicket(ticketId);
        Settings.IMP.TICKET_INFO_COMMAND.MESSAGE.forEach(message -> sender.sendMessage(StringCommon.color(message.replace("%ticketId%", ticketData.getIdTicket()).replace("%player%", ticketData.getName()).replace("%diamonds%", String.valueOf(ticketData.getDiamonds())).replace("%leftTime%", StringCommon.formatCountdownTime((ticketData.getFinalTime() - System.currentTimeMillis()) / 1000L)).replace("%reason%", ticketData.getReason()))));
        return true;
    }
}
