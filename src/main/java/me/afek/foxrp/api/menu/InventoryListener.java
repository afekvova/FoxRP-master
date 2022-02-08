package me.afek.foxrp.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof IMenu) {
            event.setCancelled(true);
            IMenu gui = (IMenu) event.getInventory().getHolder();
            gui.onClick(event.getInventory(), event.getCurrentItem(), (Player) event.getWhoClicked(), event.getRawSlot(), event.getClick());
        }
    }
}