package me.afek.foxrp.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.skinsrestorer.api.SkinsRestorerAPI;
import net.skinsrestorer.bukkit.SkinsRestorer;
import org.bukkit.entity.Player;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SkinsRestorerService {

    SkinsRestorerAPI skinsRestorerAPI = SkinsRestorerAPI.getApi();
    SkinsRestorer skinsRestorer = SkinsRestorer.getInstance();

    public void setSkin(Player player, String name) {
        
    }
}
