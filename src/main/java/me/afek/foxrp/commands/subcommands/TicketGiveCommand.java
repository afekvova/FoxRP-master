package me.afek.foxrp.commands.subcommands;

import com.google.common.base.Joiner;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.commands.SubCommand;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.database.FoxStorage;
import me.afek.foxrp.model.Ticket;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketGiveCommand extends SubCommand {

    FoxStorage foxStorage;
    DataCommon dataCommon;

    public TicketGiveCommand(FoxStorage foxStorage, DataCommon dataCommon) {
        super("give", 4, Settings.IMP.TICKET_GIVE_COMMAND.USE);
        this.foxStorage = foxStorage;
        this.dataCommon = dataCommon;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 4 || !StringCommon.isStringInt(args[1]) || !StringCommon.isStringInt(args[2])) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_GIVE_COMMAND.USE));
            return;
        }

        String playerName = args[0];
        int diamonds = Integer.parseInt(args[1]);
        int hours = Integer.parseInt(args[2]);

        if (hours > Settings.IMP.TICKET_GIVE_COMMAND.MAX_TIME_TICKET || hours < Settings.IMP.TICKET_GIVE_COMMAND.MIN_TIME_TICKET) {
            sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_GIVE_COMMAND.TIME_ERROR));
            return;
        }

        long hoursStamp = System.currentTimeMillis() + hours * (1000L * 3600L);
        String reason = Joiner.on(' ').join(Arrays.asList(Arrays.copyOfRange(args, 3, args.length)));

        Ticket ticketData = new Ticket("#" + this.generateRandomString(21), playerName, reason, diamonds, hoursStamp);
        this.dataCommon.addTicket(ticketData);
        this.foxStorage.saveTicket(ticketData);

        sender.sendMessage(StringCommon.color(Settings.IMP.TICKET_GIVE_COMMAND.SUCCESS.replace("%ticketId%", ticketData.getIdTicket()).replace("%player%", ticketData.getName())));

        Player player = Bukkit.getPlayer(playerName);
        if (player == null || !player.isOnline() || !Settings.IMP.TICKET_GIVE_COMMAND.SEND_PLAYER_MESSAGE) return;
        player.sendMessage(StringCommon.color(Settings.IMP.TICKET_GIVE_COMMAND.PLAYER_MESSAGE.replace("%ticketId%", ticketData.getIdTicket())));
    }

    private String generateRandomString(int length) {
        String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ", DIGITS = "0123456789";
        String ALPHABET = UPPER + UPPER.toLowerCase() + DIGITS;
        char[] buf = ALPHABET.toCharArray();

        StringBuilder stringBuilder = new StringBuilder();
        for (int idx = 0; idx < length; ++idx)
            stringBuilder.append(buf[idx]);

        return stringBuilder.toString();
    }
}
