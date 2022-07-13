package me.afek.foxrp.commands;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.commands.subcommands.*;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.database.FoxStorage;
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

    FoxStorage foxStorage;
    DataCommon dataCommon;

    public TicketCommand(FoxStorage foxStorage, DataCommon dataCommon) {
        this.foxStorage = foxStorage;
        this.dataCommon = dataCommon;
        this.registerSubCommands();
    }

    private void registerSubCommands() {
        this.subCommandSet.addAll(Arrays.asList(new TicketEditCommand(this.foxStorage, this.dataCommon), new TicketGiveCommand(this.foxStorage, this.dataCommon), new TicketInfoCommand(this.dataCommon), new TicketPlayerCommand(this.dataCommon), new TicketRemoveCommand(this.foxStorage, this.dataCommon)));
    }

    private void sendHelp(CommandSender sender) {
        subCommandSet.stream().filter(subCommand -> subCommand.checkPermission(sender)).map(SubCommand::getUsage).forEach(sender::sendMessage);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            this.sendHelp(sender);
            return true;
        }

        Optional<SubCommand> subCommandOptional = subCommandSet.stream().filter(subCommand -> subCommand.getLabel().equalsIgnoreCase(args[0])).findFirst();
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
