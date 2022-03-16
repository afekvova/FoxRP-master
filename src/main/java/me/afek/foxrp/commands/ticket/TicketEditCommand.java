package me.afek.foxrp.commands.ticket;

import com.google.common.base.Joiner;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.database.Sql;
import me.afek.foxrp.objects.TicketData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketEditCommand implements CommandExecutor {

    Sql sql;
    DataCommon dataCommon;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("foxrp.commands.ticketedit")) {
            sender.sendMessage(StringCommon.color(Settings.IMP.PERMISSION_MSG));
            return true;
        }

        //give [<ник> <алмазы> <срок> <причина>]
        if (args.length < 5 || !StringCommon.isStringInt(args[2]) || !StringCommon.isStringInt(args[3])) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_EDIT_COMMAND.USE));
            return true;
        }

        String ticketId = args[0];
        if (!this.dataCommon.containTicket(ticketId)) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_EDIT_COMMAND.NOT_EXIST));
            return true;
        }

        String playerName = args[1];
        int diamonds = Integer.parseInt(args[2]);
        long hours = System.currentTimeMillis() + Integer.parseInt(args[3]) * (1000L);
        String reason = Joiner.on(' ').join(Arrays.asList(Arrays.copyOfRange(args, 4, args.length)));

        TicketData ticketData = new TicketData(ticketId, playerName, reason, diamonds, hours);
        this.dataCommon.addTicket(ticketData);
        this.sql.saveTicket(ticketData);

        //success messages
        Player player = Bukkit.getPlayer(playerName);
        if (player == null || !player.isOnline()) return true;
        //send message

        return true;
    }
}
