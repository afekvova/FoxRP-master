package me.afek.foxrp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.api.menu.InventoryListener;
import me.afek.foxrp.menus.MainMenu;
import me.afek.foxrp.services.HeadService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class FoxRPPlugin extends JavaPlugin {

    @Getter
    static FoxRPPlugin instance;
    @Getter
    HeadService headService;

    @Override
    public void onEnable() {
        instance = this;
        this.headService = new HeadService();
        this.registerListeners();
        this.getCommand("test").setExecutor(this);
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new InventoryListener(), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        MainMenu mainMenu = new MainMenu(player);
        mainMenu.show(player);
        return true;
    }
}
