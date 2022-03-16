package me.afek.foxrp;

import com.earth2me.essentials.Essentials;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.api.menu.InventoryListener;
import me.afek.foxrp.commands.ticket.TicketGiveCommand;
import me.afek.foxrp.commands.ticket.TicketRemoveCommand;
import me.afek.foxrp.commands.СharacterCommand;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.database.Sql;
import me.afek.foxrp.listeners.PlayerListener;
import me.afek.foxrp.services.EssentialsService;
import me.afek.foxrp.services.PlayerDataService;
import me.afek.foxrp.services.SkinsRestorerService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class FoxRPPlugin extends JavaPlugin {

    @Getter
    static FoxRPPlugin instance;

    @Getter
    DataCommon dataCommon;
    @Getter
    PlayerDataService playerDataService;
    @Getter
    Essentials essentials;
    @Getter
    EssentialsService essentialsService;
    @Getter
    SkinsRestorerService skinsRestorerService;
    Sql sql;

    @Override
    public void onEnable() {
        instance = this;

        if (getServer().getPluginManager().getPlugin("Essentials") == null) {
            this.getLogger().severe(String.format("[%s] - Disabled due to no Essentials dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Settings.IMP.reload(new File(this.getDataFolder(), "config.yml"));

        this.essentials = (Essentials) Essentials.getProvidingPlugin(Essentials.class);
        this.essentialsService = new EssentialsService(this.essentials);
        this.skinsRestorerService = new SkinsRestorerService();

        this.dataCommon = new DataCommon();
        this.playerDataService = new PlayerDataService(this, this.dataCommon);

        this.sql = new Sql(this, this.dataCommon);

        this.registerListeners();
        this.registerCommands();
    }

    private void registerCommands() {
        this.getCommand("bampgive").setExecutor(new TicketGiveCommand(this.sql, this.dataCommon));
        this.getCommand("bampremove").setExecutor(new TicketRemoveCommand(this.sql, this.dataCommon));
        this.getCommand("character").setExecutor(new СharacterCommand());
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new InventoryListener(), this);
        pluginManager.registerEvents(new PlayerListener(this.dataCommon), this);
    }

    @Override
    public void onDisable() {
        if (this.playerDataService != null)
            this.playerDataService.savePlayerData();

        if (this.dataCommon != null)
            this.dataCommon.clearAll();

        if (this.sql != null)
            this.sql.close();
    }
}
