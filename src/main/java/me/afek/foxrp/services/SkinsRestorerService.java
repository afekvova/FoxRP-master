package me.afek.foxrp.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.skinsrestorer.api.PlayerWrapper;
import net.skinsrestorer.api.SkinsRestorerAPI;
import net.skinsrestorer.api.exception.SkinRequestException;
import net.skinsrestorer.api.property.IProperty;
import net.skinsrestorer.bukkit.SkinsRestorer;
import org.bukkit.entity.Player;

import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SkinsRestorerService {

    SkinsRestorerAPI skinsRestorerAPI = SkinsRestorerAPI.getApi();
    SkinsRestorer skinsRestorer = SkinsRestorer.getInstance();

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
        skinsRestorerAPI.getSkinStorage().setSkinData(player.getName(), property,
                (System.currentTimeMillis() + 3153600000000L));
        skinsRestorerAPI.getSkinStorage().setSkinName(player.getName(), player.getName());
        skinsRestorerAPI.applySkin(new PlayerWrapper(player), property);
        try {
            skinsRestorerAPI.applySkin(new PlayerWrapper(player));
        } catch (SkinRequestException exception) {
            exception.printStackTrace();
            return false;
        }

        return true;
    }
}
