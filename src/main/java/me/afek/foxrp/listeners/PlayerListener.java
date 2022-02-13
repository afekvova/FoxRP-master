package me.afek.foxrp.listeners;

import lombok.RequiredArgsConstructor;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.commons.StringCommon;
import me.afek.foxrp.config.Settings;
import me.afek.foxrp.objects.CharacterData;
import net.skinsrestorer.api.SkinsRestorerAPI;
import net.skinsrestorer.api.exception.SkinRequestException;
import net.skinsrestorer.api.property.IProperty;
import net.skinsrestorer.shared.utils.C;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final DataCommon dataCommon;
    private final Pattern namePattern = Pattern.compile("^[а-яА-Я_of0-9]+$");

    @EventHandler
    public void playerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        CharacterData CharacterData = this.dataCommon.getNewCharacter(player.getName());
        if (CharacterData == null) return;

        if (message.equalsIgnoreCase(StringCommon.color(Settings.IMP.MESSAGES.CREATE_CHARACTER.STOP_CREATE_WORD))) {
            this.dataCommon.removeNewCharacter(player.getName());
            player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.CREATE_CHARACTER.STOP_CREATE_SUCCESS));
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);
        if (CharacterData.getName() == null || CharacterData.getName().isEmpty()) {
            if (!this.validMojangUsername(message)) {
                player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.CREATE_CHARACTER.INVALID_NICK));
                return;
            }

            CharacterData.setName(message);
            player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.CREATE_CHARACTER.ENTER_NICK_SUCCESS));
            player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.CREATE_CHARACTER.ENTER_URL));
            return;
        }

        if (CharacterData.getValue() == null || CharacterData.getValue().isEmpty()) {
            if (!C.validUrl(message)) {
                player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.CREATE_CHARACTER.INVALID_URL));
                return;
            }

            IProperty property = null;
            try {
                property = SkinsRestorerAPI.getApi().genSkinUrl(message, "steve");
            } catch (SkinRequestException e) {
                player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.CREATE_CHARACTER.INVALID_URL));
                return;
            }
            CharacterData.setValue(property.getValue());
            CharacterData.setSignature(property.getSignature());
            this.dataCommon.removeNewCharacter(player.getName());
            this.dataCommon.addPlayerCharacter(player.getName(), CharacterData);
            player.sendMessage(StringCommon.color(Settings.IMP.MESSAGES.CREATE_CHARACTER.CREATE_CHARACTER_SUCCESS));
        }
    }

    private boolean validMojangUsername(String username) {
        return username.length() > 16 ? false : namePattern.matcher(username).matches();
    }
}
