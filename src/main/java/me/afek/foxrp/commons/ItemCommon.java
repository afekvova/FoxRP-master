package me.afek.foxrp.commons;

import net.skinsrestorer.api.bukkit.BukkitHeadAPI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemCommon {

    public static ItemStack itemStack(String materialName, String display, int amount, List<String> lore) {
        Material material = materialName.startsWith("basehead-") ? Material.PLAYER_HEAD : Material.matchMaterial(materialName);
        if (material == null) return null;

        ItemStack itemStack = new ItemStack(material, amount);
        if (materialName.startsWith("basehead-"))
            BukkitHeadAPI.setSkull(itemStack, materialName.split("-")[1]);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(StringCommon.color(display));
        if (lore != null)
            itemMeta.setLore(StringCommon.color(lore));

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
