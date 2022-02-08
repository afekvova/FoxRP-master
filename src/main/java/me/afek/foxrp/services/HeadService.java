package me.afek.foxrp.services;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HeadService {

    ConcurrentHashMap<String, ItemStack> cache;
    ItemStack head;

    public HeadService() {
        this.cache = new ConcurrentHashMap<>();
        this.head = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
    }

    public ItemStack getItem(String... args) {
        if (args.length == 0)
            return head.clone();
        if (this.cache.containsKey(args[0]))
            return this.cache.get(args[0]).clone();
        try {
            ItemStack item = head.clone();
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            GameProfile profile = getGameProfile(args[0]);
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
            item.setItemMeta(meta);
            this.cache.put(args[0], item);
            return item.clone();
        } catch (IllegalAccessException | NoSuchFieldException ex) {
            return head.clone();
        }
    }

    public GameProfile getGameProfile(String input) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", input));
        return profile;
    }
}
