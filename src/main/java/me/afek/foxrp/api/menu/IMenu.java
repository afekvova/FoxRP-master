package me.afek.foxrp.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface IMenu extends InventoryHolder {

    void onClick(final Inventory inventory, final ItemStack itemStack, final Player player, final int slot, final ClickType clickType);

    default void show(final Player player) {
        player.openInventory(getInventory());
    }

}
