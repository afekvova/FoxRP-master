package me.afek.foxrp.commands;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.commands.subcommands.*;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.database.FoxStorage;
import me.afek.foxrp.repositories.impl.TicketRepository;
import me.afek.foxrp.repositories.impl.WarningRepository;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketCommand implements CommandExecutor {

    Set<SubCommand> subCommandSet = new HashSet<>();

    public TicketCommand(FoxStorage foxStorage, TicketRepository ticketRepository, WarningRepository warningRepository) {
        this.subCommandSet.addAll(Arrays.asList(
                new TicketEditCommand(foxStorage, ticketRepository),
                new TicketGiveCommand(foxStorage, ticketRepository),
                new TicketInfoCommand(ticketRepository),
                new TicketPlayerCommand(ticketRepository, warningRepository),
                new TicketRemoveCommand(foxStorage, ticketRepository)));
    }

    private void sendHelp(CommandSender sender) {
        subCommandSet.stream().filter(subCommand -> subCommand.checkPermission(sender))
                .map(SubCommand::getUsage).forEach(sender::sendMessage);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            this.sendHelp(sender);
            return true;
        }

        Optional<SubCommand> subCommandOptional = subCommandSet.stream()
                .filter(subCommand -> subCommand.getLabel().equalsIgnoreCase(args[0])).findFirst();
        if (!subCommandOptional.isPresent()) {
            this.sendHelp(sender);
            return true;
        }

        SubCommand subCommand = subCommandOptional.get();
        if (!subCommand.checkPermission(sender)) {
            sender.sendMessage(StringCommon.color(Settings.IMP.PERMISSION_MSG));
            return true;
        }

        if (args.length < (subCommand.getMinArgs() - 1) && !(subCommand.getLabel().equalsIgnoreCase("give") || subCommand.getLabel().equalsIgnoreCase("edit"))) {
            sender.sendMessage(subCommand.getUsage());
            return true;
        }

        subCommand.execute(sender, args);
        return true;
    }
}
