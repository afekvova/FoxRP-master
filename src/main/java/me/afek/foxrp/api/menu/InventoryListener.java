package me.afek.foxrp.api.menu;

import lombok.AllArgsConstructor;
import me.afek.foxrp.FoxRPPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

@AllArgsConstructor
public class InventoryListener implements Listener {

    FoxRPPlugin plugin;

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(final InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof IMenu)) return;

        event.setCancelled(true);

        ((IMenu) event.getInventory().getHolder()).onClick(event.getInventory(), event.getCurrentItem(), (Player) event.getWhoClicked(), event.getRawSlot(), event.getClick());
    }

}