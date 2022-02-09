package me.afek.foxrp.commons;

import net.skinsrestorer.api.bukkit.BukkitHeadAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ItemCommon {

    public static ItemStack getItem(String id, String name, int amount, String... lore) {
        ItemStack itemStack;
        String[] idSplit = id.split(":");
        String materialId = idSplit[0];
        if (idSplit.length == 2) {
            int subId = Integer.parseInt(idSplit[1]);
            itemStack = new ItemStack(Material.getMaterial(materialId), amount, (short) subId);
        } else {
            if (materialId.startsWith("basehead-")) {
                String[] parts = materialId.split("-");
                itemStack = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
                BukkitHeadAPI.setSkull(itemStack, parts[1]);
            } else {
                itemStack = new ItemStack(Material.getMaterial(materialId), amount);
            }
        }

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(StringCommon.color(name));
        if (lore != null && lore.length > 0)
            meta.setLore(Arrays.stream(lore).map(StringCommon::color).collect(Collectors.toList()));

        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
