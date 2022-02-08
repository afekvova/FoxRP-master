package me.afek.foxrp.commons;

import org.bukkit.ChatColor;

public class StringCommon {

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
