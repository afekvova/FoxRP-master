package me.afek.foxrp.commons;

import me.afek.foxrp.config.Settings;
import org.bukkit.ChatColor;

public class StringCommon {

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string.replace("%prefix%", Settings.IMP.PREFIX));
    }
}
