package me.afek.foxrp.commands;

import lombok.RequiredArgsConstructor;
import me.afek.foxrp.FoxRPPlugin;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.menus.CharacterChooseMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@me.afek.foxrp.commands.Command(command = "/character")
public class СharacterCommand implements CommandExecutor {

    private final FoxRPPlugin foxRPPlugin;

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

        new CharacterChooseMenu(this.foxRPPlugin, false).show(player);
        return true;
    }
}