package me.afek.foxrp;

import com.earth2me.essentials.Essentials;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.val;
import me.afek.foxrp.commands.Command;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.database.FoxStorage;
import me.afek.foxrp.database.storage.StorageType;
import me.afek.foxrp.database.storage.sqlite.SQLiteFoxStorage;
import me.afek.foxrp.repositories.Repository;
import me.afek.foxrp.repositories.impl.CharacterRepository;
import me.afek.foxrp.repositories.impl.TicketRepository;
import me.afek.foxrp.repositories.impl.WarningRepository;
import me.afek.foxrp.services.EssentialsService;
import me.afek.foxrp.services.PlayerDataService;
import me.afek.foxrp.services.SkinsRestorerService;
import me.afek.foxrp.tasks.PlayerTicketsTask;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.io.File;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public final class FoxRPPlugin extends JavaPlugin {

    TicketRepository ticketRepository;
    CharacterRepository characterRepository;
    WarningRepository warningRepository;

    PlayerDataService playerDataService;
    EssentialsService essentialsService;
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

        this.essentialsService = new EssentialsService((Essentials) Essentials.getProvidingPlugin(Essentials.class)); // Содержит методы из Essentials
        this.skinsRestorerService = new SkinsRestorerService(); // Содержит методы из SkinsRestorer

        new Reflections("me.afek.foxrp.repositories.impl").getTypesAnnotatedWith(Repository.class).forEach(clazz -> {
            try {
                val className = clazz.getName();
                FoxRPPlugin.this.getClass().getField(Character.toLowerCase(className.charAt(0)) + className.substring(1)).set(FoxRPPlugin.this, clazz.newInstance());
            } catch (Exception ignored) {
            }
        });

        this.playerDataService = new PlayerDataService(this);

        //TODO: Добавить поддержку типов базы данных
        this.foxStorage = new SQLiteFoxStorage(this);

        // Если мы не подключились к базе то отключаем плагин
        if (!this.foxStorage.connect()) {
            System.out.printf("Can't connect to database [%s]", StorageType.SQLITE.name());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Проверка тикетов
        Bukkit.getScheduler().runTaskTimer(
                this,
                new PlayerTicketsTask(this.ticketRepository, this.warningRepository, this.foxStorage),
                20L, 20L
        );

        val pluginManager = Bukkit.getPluginManager();
        new Reflections("me.afek.foxrp").getSubTypesOf(Listener.class).forEach(clazz -> {
            try {
                pluginManager.registerEvents(clazz.getConstructor(FoxRPPlugin.class).newInstance(this), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        new Reflections("me.afek.foxrp.commands").getTypesAnnotatedWith(Command.class).forEach(clazz -> {
            try {
                val annotation = clazz.getDeclaredAnnotation(Command.class);
                getCommand(annotation.command()).setExecutor((CommandExecutor) clazz.getConstructor(FoxRPPlugin.class).newInstance(this));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDisable() {
        if (this.playerDataService != null)
            this.playerDataService.savePlayerData();

        if (this.foxStorage != null)
            this.foxStorage.disconnect();
    }
}
