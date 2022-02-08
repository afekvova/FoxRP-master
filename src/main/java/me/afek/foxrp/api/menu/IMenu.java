package me.afek.foxrp.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface IMenu extends InventoryHolder {

    public void onClick(Inventory inventory, ItemStack itemStack, Player player, int slot, ClickType clickType);

    public default void show(Player player) {
        player.openInventory(getInventory());
    }
}
