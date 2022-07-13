package me.afek.foxrp.listeners;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import me.afek.foxrp.FoxRPPlugin;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.model.Character;
import me.afek.foxrp.model.Ticket;
import net.skinsrestorer.api.SkinVariant;
import net.skinsrestorer.api.SkinsRestorerAPI;
import net.skinsrestorer.api.exception.SkinRequestException;
import net.skinsrestorer.api.property.IProperty;
import net.skinsrestorer.shared.utils.C;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerListener implements Listener {

    static Pattern NAME_PATTERN = Pattern.compile("^[а-яА-Я_of]+$");

    FoxRPPlugin plugin;

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        val player = event.getPlayer();

        val ticket = this.getTicketByPlayer(player.getName());
        if (ticket == null) return;

        player.sendMessage(StringCommon.color(String.format("{PRFX} У вас активный тикет! (%s)", ticket.getIdTicket())));
    }

    @EventHandler
    private void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        val characterRepository = plugin.getCharacterRepository();

        Player player = event.getPlayer();
        String message = event.getMessage();
        Character character = characterRepository.getData(player.getName());
        if (character == null) return;

        if (message.equalsIgnoreCase(StringCommon.color(Settings.IMP.MESSAGES.CREATE_CHARACTER.STOP_CREATE_WORD))) {
            characterRepository.removeData(player.getName());
            player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.CREATE_CHARACTER.STOP_CREATE_SUCCESS));
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);
        if (character.getName() == null || character.getName().isEmpty()) {
            if (!this.validMojangUsername(message)) {
                player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.CREATE_CHARACTER.INVALID_NICK));
                return;
            }

            character.setName(message);
            player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.CREATE_CHARACTER.ENTER_NICK_SUCCESS));
            player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.CREATE_CHARACTER.ENTER_URL));
            return;
        }

        if (character.getValue() == null || character.getValue().isEmpty()) {
            if (!C.validUrl(message)) {
                player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.CREATE_CHARACTER.INVALID_URL));
                return;
            }

            IProperty property;
            try {
                property = SkinsRestorerAPI.getApi().genSkinUrl(message, SkinVariant.CLASSIC);
            } catch (SkinRequestException e) {
                player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.CREATE_CHARACTER.INVALID_URL));
                return;
            }

            character.setValue(property.getValue());
            character.setSignature(property.getSignature());
            characterRepository.removeData(player.getName());
            characterRepository.addPlayerCharacter(player.getName(), character);
            player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.CREATE_CHARACTER.CREATE_CHARACTER_SUCCESS));
        }
    }

    private boolean validMojangUsername(final String username) {
        return username.length() <= 16 && NAME_PATTERN.matcher(username).matches();
    }

    private Ticket getTicketByPlayer(final String playerName) {
        for (val ticket : plugin.getTicketRepository().getData()) {
            if (!ticket.getName().equalsIgnoreCase(playerName.toLowerCase())) continue;

            return ticket;
        }

        return null;
    }

}
