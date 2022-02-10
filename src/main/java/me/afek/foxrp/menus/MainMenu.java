package me.afek.foxrp.menus;

import me.afek.foxrp.FoxRPPlugin;
import me.afek.foxrp.api.menu.IMenu;
import me.afek.foxrp.commons.ItemCommon;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.objects.HeroData;
import net.skinsrestorer.api.PlayerWrapper;
import net.skinsrestorer.api.SkinsRestorerAPI;
import net.skinsrestorer.api.bukkit.BukkitHeadAPI;
import net.skinsrestorer.api.property.BukkitProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainMenu implements IMenu {

    private final FoxRPPlugin plugin = FoxRPPlugin.getInstance();
    private final Inventory inventory;
    private final boolean deleteMenu;
    private int page = 1;

    public MainMenu(boolean deleteMenu) {
        this.inventory = Bukkit.createInventory(this, 9 * 6, StringCommon.color("&cВыберите персонажа"));
        this.loadItems();
        this.deleteMenu = deleteMenu;
    }

    private void loadItems() {
        this.clearItems();
        this.inventory.setItem(3, ItemCommon.getItem("basehead-eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM5MDZkNjg4ZTY1ODAyNTY5ZDk3MDViNTc5YmNlNTZlZGM4NmVhNWMzNmJkZDZkNmZjMzU1MTZhNzdkNCJ9fX0=", "&aСоздать персонажа", 1, "&7", "&7Нажмите, чтобы создать персонажа"));
        this.inventory.setItem(4, ItemCommon.getItem(Material.BARRIER.name(), "&eОтключить персонажа", 1, "&7", "&7Нажмите, чтобы отключить персонажа"));
        this.inventory.setItem(5, ItemCommon.getItem("basehead-eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmI3OGZhNWRlZmU3MmRlYmNkOWM3NmFiOWY0ZTExNDI1MDQ3OWJiOWI0NGY0Mjg4N2JiZjZmNzM4NjEyYiJ9fX0=", "&cУдалить персонажа", 1, "&7", "&7Нажмите, чтобы удалить персонажа"));
        this.inventory.setItem(49, ItemCommon.getItem(Material.BOOK.name(), "&cСтраница номер " + this.page, 1));
    }

    @Override
    public void onClick(Inventory inventory, ItemStack itemStack, Player player, int slot, ClickType clickType) {
        if (slot == 53 && itemStack != null && itemStack.getType() != Material.AIR) {
            ++page;
            this.updateInventory(player);
            return;
        }

        if (slot == 45 && itemStack != null && itemStack.getType() != Material.AIR) {
            if (page > 1)
                --page;

            this.updateInventory(player);
            return;
        }

        if (itemStack.getType() == Material.SKULL_ITEM) {
            int index = slot - 9 + (36 * (this.page - 1));
            HeroData heroData = this.plugin.getDataCommon().getPlayerHeroes(player.getName()).get(index);
            if (heroData == null) return;

            SkinsRestorerAPI.getApi().applySkin(new PlayerWrapper(player), new BukkitProperty(player.getName(), heroData.getValue()));
        }
    }

    public void show(Player player) {
        this.updateInventory(player);
        player.openInventory(this.inventory);
    }

    public void updateInventory(Player player) {
        loadItems();
        List<HeroData> dataList = this.plugin.getDataCommon().getPlayerHeroes(player.getName());
        if (dataList == null)
            dataList = new ArrayList<>();

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
        skullMeta.setDisplayName(StringCommon.color("&c" + heroData.getName()));
        itemStack.setItemMeta(skullMeta);
        BukkitHeadAPI.setSkull(itemStack, heroData.getValue());
        return itemStack;
    }

    private void clearItems() {
        for (int i = 9; i <= 53; i++)
            this.inventory.setItem(i, new ItemStack(Material.AIR));
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
