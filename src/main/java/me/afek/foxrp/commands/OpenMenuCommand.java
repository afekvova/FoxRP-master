package me.afek.foxrp.commands;

import me.afek.foxrp.menus.MainMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenMenuCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MainMenu mainMenu = new MainMenu(false);
        mainMenu.show((Player) sender);
        return true;
    }
}
