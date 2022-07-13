package me.afek.foxrp.commons;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CooldownCommon {
    
    String player, key;
    long cooldown;

    public static HashMap<String, CooldownCommon> cooldowns = new HashMap<>();

    public static void setCooldown(String player, long cooldown, String title) {
        cooldowns.put(player + title, new CooldownCommon(player, title, System.currentTimeMillis() + cooldown));
    }

    public static boolean hasCooldown(String player, String title) {
        return cooldowns.get(player + title) != null && cooldowns.get(player + title).getCooldown() > System.currentTimeMillis();
    }

    public static long getCooldown(String player, String title) {
        return (cooldowns.get(player + title).getCooldown() - System.currentTimeMillis()) / 1000L;
    }
}