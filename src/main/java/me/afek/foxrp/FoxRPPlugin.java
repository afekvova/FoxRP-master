package me.afek.foxrp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.api.menu.InventoryListener;
import me.afek.foxrp.commands.OpenMenuCommand;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.listeners.PlayerListener;
import me.afek.foxrp.objects.HeroData;
import me.afek.foxrp.services.PlayerDataService;
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
    DataCommon dataCommon;
    @Getter
    PlayerDataService playerDataService;


    @Override
    public void onEnable() {
        instance = this;

        this.dataCommon = new DataCommon();
        this.playerDataService = new PlayerDataService(this, this.dataCommon);

        this.registerListeners();
        this.getCommand("test").setExecutor(this);
        this.getCommand("openmenu").setExecutor(new OpenMenuCommand());
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new InventoryListener(), this);
        pluginManager.registerEvents(new PlayerListener(this.dataCommon), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
//        SkinsRestorerAPI.getApi().applySkin(player.getName(), new BukkitProperty(player.getName(), ));
        this.dataCommon.addNewHero(sender.getName(), new HeroData(null, null));
        player.sendMessage("Напиши в чат ник!");
        player.sendMessage("Если хотите отменить добавление, напишите 'отменить'!");
        return true;
    }

    @Override
    public void onDisable() {
        if (this.playerDataService != null)
            this.playerDataService.savePlayerData();
        if (this.dataCommon != null)
            this.dataCommon.clearAll();
    }
}
