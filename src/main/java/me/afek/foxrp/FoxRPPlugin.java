package me.afek.foxrp;

import com.earth2me.essentials.Essentials;
import lombok.Getter;
import me.afek.foxrp.api.menu.InventoryListener;
import me.afek.foxrp.commands.TicketCommand;
import me.afek.foxrp.commands.СharacterCommand;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.database.FoxStorage;
import me.afek.foxrp.database.storage.StorageType;
import me.afek.foxrp.database.storage.sqlite.SQLiteFoxStorage;
import me.afek.foxrp.listeners.PlayerListener;
import me.afek.foxrp.services.EssentialsService;
import me.afek.foxrp.services.PlayerDataService;
import me.afek.foxrp.services.SkinsRestorerService;
import me.afek.foxrp.tasks.PlayerTicketsTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;

public final class FoxRPPlugin extends JavaPlugin {

    @Getter
    DataCommon dataCommon;
    PlayerDataService playerDataService;
    @Getter
    EssentialsService essentialsService;
    @Getter
    SkinsRestorerService skinsRestorerService;
    FoxStorage foxStorage;

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("Essentials") == null) {
            this.getLogger().severe(String.format("[%s] - Disabled due to no Essentials dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Settings.IMP.reload(new File(this.getDataFolder(), "config.yml")); // Загружаем конфиг

        Essentials essentials = (Essentials) Essentials.getProvidingPlugin(Essentials.class);
        this.essentialsService = new EssentialsService(essentials); // Содержит методы из Essentials
        this.skinsRestorerService = new SkinsRestorerService(); // Содержит методы из SkinsRestorer

        this.dataCommon = new DataCommon(); // Репозиторий данных
        this.playerDataService = new PlayerDataService(this, this.dataCommon); //

        //TODO: Добавить поддержку типов базы данных
        this.foxStorage = new SQLiteFoxStorage(this, this.dataCommon);
        // Если мы не подключились к базе то отключаем плагин
        if (!this.foxStorage.connect()) {
            System.out.printf("Can't connect to database [%s]", StorageType.SQLITE.name());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Проверка тикетов
        Bukkit.getScheduler().runTaskTimer(this, new PlayerTicketsTask(this.dataCommon, this.foxStorage), 20L, 20L);

        Arrays.asList(new InventoryListener(), new PlayerListener(this.dataCommon)).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this)); // register listeners
        this.registerCommands();
    }

    private void registerCommands() {
        this.getCommand("ticket").setExecutor(new TicketCommand(this.foxStorage, this.dataCommon));
        this.getCommand("character").setExecutor(new СharacterCommand(this));
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
