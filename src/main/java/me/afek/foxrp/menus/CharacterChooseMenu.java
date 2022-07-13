package me.afek.foxrp.menus;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import me.afek.foxrp.FoxRPPlugin;
import me.afek.foxrp.api.menu.IMenu;
import me.afek.foxrp.commons.CooldownCommon;
import me.afek.foxrp.commons.ItemCommon;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.model.Character;
import net.skinsrestorer.api.bukkit.BukkitHeadAPI;
import net.skinsrestorer.bukkit.utils.BukkitProperty;
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

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CharacterChooseMenu implements IMenu {
    FoxRPPlugin plugin;

    @NonFinal
    Inventory inventory;
    @NonFinal
    boolean delete;
    @NonFinal
    int page = 1;

    public CharacterChooseMenu(FoxRPPlugin plugin, boolean delete) {
        this.plugin = plugin;
        this.delete = delete;

        this.inventory = Bukkit.createInventory(this, 9 * 6, StringCommon.color(delete ? Settings.IMP.MENU_SETTINGS.DISPLAYNAME_DELETE : Settings.IMP.MENU_SETTINGS.DISPLAYNAME));
        this.loadItems();
    }

    private void loadItems() {
        this.clearItems();
        this.inventory.setItem(3, ItemCommon.itemStack(Settings.IMP.MENU_SETTINGS.CREATE_CHARACTER_ICON.MATERIAL, Settings.IMP.MENU_SETTINGS.CREATE_CHARACTER_ICON.DISPLAYNAME, 1, Settings.IMP.MENU_SETTINGS.CREATE_CHARACTER_ICON.LORE));
        this.inventory.setItem(4, ItemCommon.itemStack(Settings.IMP.MENU_SETTINGS.CLEAR_CHARACTER_ICON.MATERIAL, Settings.IMP.MENU_SETTINGS.CLEAR_CHARACTER_ICON.DISPLAYNAME, 1, Settings.IMP.MENU_SETTINGS.CLEAR_CHARACTER_ICON.LORE));
        this.inventory.setItem(5, ItemCommon.itemStack(Settings.IMP.MENU_SETTINGS.DELETE_CHARACTER_ICON.MATERIAL, Settings.IMP.MENU_SETTINGS.DELETE_CHARACTER_ICON.DISPLAYNAME, 1, Settings.IMP.MENU_SETTINGS.DELETE_CHARACTER_ICON.LORE));
        this.inventory.setItem(49, ItemCommon.itemStack(Settings.IMP.MENU_SETTINGS.PAGE_ICON.MATERIAL, Settings.IMP.MENU_SETTINGS.PAGE_ICON.DISPLAYNAME.replace("%number%", String.valueOf(this.page)), 1, Settings.IMP.MENU_SETTINGS.PAGE_ICON.LORE));
    }

    @Override
    public void onClick(Inventory inventory, ItemStack itemStack, Player player, int slot, ClickType clickType) {
        if (itemStack == null || itemStack.getType() != Material.AIR) return;
        if (slot == 53) {
            ++page;
            this.updateInventory(player);
            return;
        }

        if (slot == 45) {
            if (page > 1)
                --page;

            this.updateInventory(player);
            return;
        }

        if (slot == 5) {
            this.delete = true;
            this.inventory = Bukkit.createInventory(this, 9 * 6, StringCommon.color(Settings.IMP.MENU_SETTINGS.DISPLAYNAME_DELETE));

            player.closeInventory();
            this.show(player);
            return;
        }

        if (slot == 3) {
            this.plugin.getCharacterRepository().addData(player.getName(), new Character(null, null, null));
            player.closeInventory();
            player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.CREATE_CHARACTER.START_CRATE));
            player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.CREATE_CHARACTER.STOP_CREATE));
            return;
        }

        if (slot == 4) {
            this.plugin.getSkinsRestorerService().clearSkin(player);
            this.plugin.getEssentialsService().setPlayerName(player, null);
            player.closeInventory();
            return;
        }

        if (itemStack.getType() == Material.PLAYER_HEAD) {
            if (CooldownCommon.hasCooldown(player.getName(), "choosecharacter") && !this.delete) {
                player.closeInventory();
                player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.COOLDOWN_MESSAGE.replace("%time%", StringCommon.formatCountdownTime(CooldownCommon.getCooldown(player.getName(), "choosecharacter")))));
                return;
            }

            int index = slot - 9 + (36 * (this.page - 1));
            List<Character> playerData = this.plugin.getCharacterRepository().getPlayerCharacters(player.getName());

            if (this.delete) {
                playerData.remove(index);
                player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.SUCCESS_DELETE_CHARACTER));
                player.closeInventory();
                return;
            }

            Character character = playerData.get(index);
            if (character == null) return;

            if (this.plugin.getSkinsRestorerService().setSkin(player, new BukkitProperty(player.getName(), character.getValue(), character.getSignature()))) {
                this.plugin.getEssentialsService().setPlayerName(player, character.getName());
                CooldownCommon.setCooldown(player.getName(), 30 * 1000L, "choosecharacter"); // 30 секунд

                player.closeInventory();
                player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.SUCCESS_ENTER_CHARACTER.replace("%nick%", character.getName())));
            }
        }
    }

    public void show(Player player) {
        this.updateInventory(player);
        player.openInventory(this.inventory);
    }

    public void updateInventory(Player player) {
        loadItems();
        List<Character> dataList = this.plugin.getCharacterRepository().getPlayerCharacters(player.getName());
        if (dataList == null)
            dataList = new ArrayList<>();

        int size = dataList.size();
        int index = page * 36 - 36 > size ? 1 : page * 36 - 36;
        int endIndex = Math.min((index + 36), size);
        int slot = 9;
        dataList = dataList.stream().skip(index).limit(endIndex - index).collect(Collectors.toList());
        for (Character characterData : dataList) {
            inventory.setItem(slot, this.getHead(characterData));
            ++slot;
        }

        if (size > endIndex)
            this.inventory.setItem(53, ItemCommon.itemStack(Settings.IMP.MENU_SETTINGS.NEXT_PAGE_ICON.MATERIAL, Settings.IMP.MENU_SETTINGS.NEXT_PAGE_ICON.DISPLAYNAME, 1, Settings.IMP.MENU_SETTINGS.NEXT_PAGE_ICON.LORE));

        if (page > 1)
            this.inventory.setItem(45, ItemCommon.itemStack(Settings.IMP.MENU_SETTINGS.PREVIOUS_PAGE_ICON.MATERIAL, Settings.IMP.MENU_SETTINGS.PREVIOUS_PAGE_ICON.DISPLAYNAME, 1, Settings.IMP.MENU_SETTINGS.PREVIOUS_PAGE_ICON.LORE));
    }

    private ItemStack getHead(Character characterData) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setDisplayName(StringCommon.color(Settings.IMP.MENU_SETTINGS.CHARACTER_ICON.DISPLAYNAME.replace("%name%", characterData.getName())));
        skullMeta.setLore(Settings.IMP.MENU_SETTINGS.CHARACTER_ICON.LORE.stream().map(string -> StringCommon.color(string.replace("%name%", characterData.getName()))).collect(Collectors.toList()));
        itemStack.setItemMeta(skullMeta);
        BukkitHeadAPI.setSkull(itemStack, characterData.getValue());
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
