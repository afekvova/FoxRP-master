package me.afek.foxrp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.api.menu.InventoryListener;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.menus.MainMenu;
import me.afek.foxrp.mojang.MineSkinAPI;
import me.afek.foxrp.mojang.MojangAPI;
import me.afek.foxrp.objects.HeroData;
import me.afek.foxrp.services.PlayerDataService;
import net.skinsrestorer.api.serverinfo.Platform;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class FoxRPPlugin extends JavaPlugin {

    @Getter
    static FoxRPPlugin instance;
    @Getter
    DataCommon dataCommon;
    @Getter
    PlayerDataService playerDataService;

    @Getter
    MojangAPI mojangAPI;
    @Getter
    MineSkinAPI mineSkinAPI;

    @Override
    public void onEnable() {
        instance = this;

        this.mojangAPI = new MojangAPI(Platform.BUKKIT);
        this.mineSkinAPI = new MineSkinAPI(this.mojangAPI);

        this.dataCommon = new DataCommon();
        this.playerDataService = new PlayerDataService(this, this.dataCommon);

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
        List<HeroData> dataList = new ArrayList<>();
        for (int i = 0; i < 250; i++)
            dataList.add(new HeroData("test" + i, "test"));

        this.getDataCommon().addPlayerHeroes(player.getName(), dataList);
        MainMenu mainMenu = new MainMenu(false);
        mainMenu.show(player);
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
