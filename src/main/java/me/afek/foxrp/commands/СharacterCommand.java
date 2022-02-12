package me.afek.foxrp.commands;

import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.menus.CharacterChooseMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class СharacterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(StringCommon.color("&cYou are not a player!"));
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("foxrp.commands.character")) {
            player.sendMessage(StringCommon.color(Settings.IMP.PERMISSION_MSG));
            return true;
        }

        CharacterChooseMenu mainMenu = new CharacterChooseMenu(false);
        mainMenu.show((Player) sender);
        return true;
    }
}
