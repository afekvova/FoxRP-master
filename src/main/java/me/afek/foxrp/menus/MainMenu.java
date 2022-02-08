package me.afek.foxrp.menus;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.api.menu.IMenu;
import me.afek.foxrp.commons.ItemCommon;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.objects.HeroData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainMenu implements IMenu {

    Inventory inventory;
    int page = 1;
    boolean deleteMenu;

    public MainMenu(boolean deleteMenu) {
        this.inventory = Bukkit.createInventory(this, 9 * 6, StringCommon.color("&cВыберите персонажа"));
        this.loadItems();
        this.deleteMenu = deleteMenu;
    }

    private void loadItems() {
        this.inventory.setItem(3, ItemCommon.getItem("basehead-eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM5MDZkNjg4ZTY1ODAyNTY5ZDk3MDViNTc5YmNlNTZlZGM4NmVhNWMzNmJkZDZkNmZjMzU1MTZhNzdkNCJ9fX0=", "&aСоздать персонажа", 1, "&7", "&7Нажмите, чтобы создать персонажа"));
        this.inventory.setItem(4, ItemCommon.getItem(Material.BARRIER.name(), "&eОтключить персонажа", 1, "&7", "&7Нажмите, чтобы отключить персонажа"));
        this.inventory.setItem(5, ItemCommon.getItem("basehead-eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmI3OGZhNWRlZmU3MmRlYmNkOWM3NmFiOWY0ZTExNDI1MDQ3OWJiOWI0NGY0Mjg4N2JiZjZmNzM4NjEyYiJ9fX0=", "&cУдалить персонажа", 1, "&7", "&7Нажмите, чтобы удалить персонажа"));
        this.inventory.setItem(49, ItemCommon.getItem(Material.BOOK.name(), "&cСтраница номер " + this.page, 1));
    }

    @Override
    public void onClick(Inventory inventory, ItemStack itemStack, Player player, int slot, ClickType clickType) {
        if (slot == 53 && itemStack != null && itemStack.getType() != Material.AIR) {
            ++page;
            this.updateInventory();
        }

        if (slot == 45 && itemStack != null && itemStack.getType() != Material.AIR) {
            if (page > 1)
                --page;
            this.updateInventory();
        }
    }

    public void show(Player player) {
        this.updateInventory();
        player.openInventory(this.inventory);
    }

    public void updateInventory() {
        this.inventory.clear();
        loadItems();

        int size = dataList.size();
        int index = page * 36 - 36 > size ? 1 : page * 36 - 36;
        int endIndex = Math.min((index + 36), size);
        int slot = 9;
        dataList = dataList.stream().skip(index).limit(endIndex - index).collect(Collectors.toList());
        for (HeroData heroData : dataList) {
            inventory.setItem(slot, this.getHead(heroData));
            ++slot;
        }

        if (size > endIndex)
            this.inventory.setItem(53, ItemCommon.getItem(Material.ARROW.name(), "&aСлед. страница", 1));

        if (page > 1)
            this.inventory.setItem(45, ItemCommon.getItem(Material.ARROW.name(), "&cПред. страница", 1));
    }

    private ItemStack getHead(HeroData heroData) {
        ItemStack itemStack = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwner(heroData.getValue());
        skullMeta.setDisplayName(StringCommon.color("&c" + heroData.getName()));
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
