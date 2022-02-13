package me.afek.foxrp.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.FoxRPPlugin;
import net.skinsrestorer.api.PlayerWrapper;
import net.skinsrestorer.api.SkinsRestorerAPI;
import net.skinsrestorer.api.exception.SkinRequestException;
import net.skinsrestorer.api.property.IProperty;
import net.skinsrestorer.bukkit.SkinsRestorer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SkinsRestorerService {

    SkinsRestorerAPI skinsRestorerAPI = SkinsRestorerAPI.getApi();
    SkinsRestorer skinsRestorer = SkinsRestorer.getInstance();

    public boolean setDefaultSkin(Player player) {
        IProperty property = this.getMojangSkin(player);
        if (property == null) return false;
        this.setSkin(player, property);
        return true;
    }

    public IProperty getMojangSkin(Player player) {
        try {
            return this.skinsRestorer.getMojangAPI().getSkin(player.getName()).get();
        } catch (SkinRequestException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void removeSkin(Player player) {
        skinsRestorerAPI.removeSkin(player.getName());
    }

    public boolean setSkin(Player player, String name) {
        try {
            Optional<IProperty> defaultSkin = SkinsRestorer.getInstance().getMojangAPI().getSkin(player.getName());
            this.setSkin(player, defaultSkin.get());
        } catch (SkinRequestException exception) {
            exception.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean setSkin(Player player, IProperty property) {
        Bukkit.getScheduler().runTaskAsynchronously(FoxRPPlugin.getInstance(), () -> {
            skinsRestorerAPI.getSkinStorage().setSkinData(player.getName(), property,
                    (System.currentTimeMillis() + 3153600000000L));
            skinsRestorerAPI.getSkinStorage().setSkinName(player.getName(), player.getName());
            skinsRestorerAPI.applySkin(new PlayerWrapper(player), property);
            try {
                skinsRestorerAPI.applySkin(new PlayerWrapper(player));
            } catch (SkinRequestException exception) {
                exception.printStackTrace();
            }
        });
        return true;
    }

    public boolean clearSkin(Player player) {
        skinsRestorerAPI.applySkin(new PlayerWrapper(player), SkinsRestorer.getInstance().getMojangAPI().createProperty("textures", "", ""));
        SkinsRestorer.getInstance().getSkinApplierBukkit().updateSkin(player);
        return true;
    }
}
