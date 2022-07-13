package me.afek.foxrp;

import com.earth2me.essentials.Essentials;
import lombok.Getter;
import me.afek.foxrp.api.menu.InventoryListener;
import me.afek.foxrp.commands.TicketCommand;
import me.afek.foxrp.commands.subcommands.*;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.database.FoxStorage;
import me.afek.foxrp.database.storage.sqlite.SQLiteFoxStorage;
import me.afek.foxrp.listeners.PlayerListener;
import me.afek.foxrp.services.EssentialsService;
import me.afek.foxrp.services.PlayerDataService;
import me.afek.foxrp.services.SkinsRestorerService;
import me.afek.foxrp.utils.PlayerTicketsTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;

public final class FoxRPPlugin extends JavaPlugin {

    @Getter
    static FoxRPPlugin instance;

    DataCommon dataCommon;
    @Getter
    PlayerDataService playerDataService;
    @Getter
    Essentials essentials;
    @Getter
    EssentialsService essentialsService;
    @Getter
    SkinsRestorerService skinsRestorerService;
    FoxStorage foxStorage;

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

        //TODO: Добавить поддержку типов базы данных
        this.foxStorage = new SQLiteFoxStorage(this, this.dataCommon);

        Bukkit.getScheduler().runTaskTimer(this, new PlayerTicketsTask(this.dataCommon, this.foxStorage), 20L, 20L);

        Arrays.asList(new InventoryListener(), new PlayerListener(this.dataCommon)).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this)); // register listeners
        this.registerCommands();
    }

    private void registerCommands() {
        this.getCommand("bampplayer").setExecutor(new TicketPlayerCommand(this.dataCommon));
        this.getCommand("bampedit").setExecutor(new TicketEditCommand(this.sql, this.dataCommon));
        this.getCommand("bampinfo").setExecutor(new TicketInfoCommand(this.dataCommon));
        this.getCommand("bampgive").setExecutor(new TicketGiveCommand(this.sql, this.dataCommon));
        this.getCommand("bampremove").setExecutor(new TicketRemoveCommand(this.sql, this.dataCommon));
        this.getCommand("character").setExecutor(new TicketCommand());
    }

    @Override
    public void onDisable() {
        if (this.playerDataService != null)
            this.playerDataService.savePlayerData();

        if (this.dataCommon != null)
            this.dataCommon.clearAll();

        if (this.foxStorage != null)
            this.foxStorage.disconnect();
    }
}
