package me.afek.foxrp.commons;

import me.afek.foxrp.config.Settings;
import org.bukkit.ChatColor;

public class StringCommon {

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string.replace("%prefix%", Settings.IMP.PREFIX).replace("{PREFIX}", Settings.IMP.PREFIX).replace("{PRFX}", Settings.IMP.PREFIX));
    }

    public static boolean isStringInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static String padezh(String ed, String a, String b, String c, int n) {
        if (n < 0)
            n = -n;
        int last = n % 100;
        if (last > 10 && last < 21)
            return ed + c;
        last = n % 10;
        if (last == 0 || last > 4)
            return ed + c;
        if (last == 1)
            return ed + a;
        if (last < 5)
            return ed + b;
        return ed + c;
    }

    public static String formatCountdownTime(long l) {
        String out = "";
        if (l >= 3600L) {
            int h = (int) (l / 3600.0D);
            out = h + " " + padezh("час", "", "а", "ов", h);
            l -= (h * 3600);
        }

        if (l >= 60L) {
            int m = (int) (l / 60.0D);
            out = out + ((out.length() > 0) ? ", " : "") + m + " " + padezh("минут", "у", "ы", "", m);
            l -= (m * 60);
        }

        if (out.length() == 0 || l > 0L)
            out = out + ((out.length() > 0) ? ", " : "") + l + " " + padezh("секунд", "у", "ы", "", (int) l);

        return out;
    }
}
