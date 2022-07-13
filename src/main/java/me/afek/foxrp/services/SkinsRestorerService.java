package me.afek.foxrp.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.skinsrestorer.api.PlayerWrapper;
import net.skinsrestorer.api.SkinsRestorerAPI;
import net.skinsrestorer.api.exception.SkinRequestException;
import net.skinsrestorer.api.property.GenericProperty;
import net.skinsrestorer.api.property.IProperty;
import net.skinsrestorer.bukkit.SkinsRestorer;
import org.bukkit.entity.Player;

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
    
    public boolean setSkin(Player player, IProperty property) {
        String skinentry = " " + player.getName();
        if (skinentry.length() > 16)
            skinentry = skinentry.substring(0, 16);

        try {
            skinsRestorerAPI.setSkinData(skinentry, new GenericProperty("textures", property.getValue(), property.getSignature()), null);
            skinsRestorerAPI.setSkin(player.getName(), skinentry);
            skinsRestorerAPI.applySkin(new PlayerWrapper(player));
        } catch (SkinRequestException e) {
            return false;
        }
        return true;
    }

    public boolean clearSkin(Player player) {
        this.removeSkin(player);
        this.setDefaultSkin(player);
        return true;
    }
}
