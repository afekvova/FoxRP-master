package me.afek.foxrp.listeners;

import lombok.RequiredArgsConstructor;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.objects.HeroData;
import net.skinsrestorer.shared.utils.C;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final DataCommon dataCommon;

    @EventHandler
    public void playerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        HeroData heroData = this.dataCommon.getNewHero(player.getName());
        if (heroData == null) return;

        if (message.equalsIgnoreCase("отмена")) {
            this.dataCommon.removeNewHero(player.getName());
            player.sendMessage("Вы отменили добавление нового героя!");
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);
        if (heroData.getName() == null || heroData.getName().isEmpty()) {
            if (!C.validMojangUsername(message)) {
                player.sendMessage("Это не ник!");
                return;
            }

            heroData.setName(message);
            player.sendMessage("Вы успешно добавили ник!");
            player.sendMessage("Введите ссылку:");
            return;
        }

        if (heroData.getValue() == null || heroData.getValue().isEmpty()) {
            if (!C.validUrl(message)) {
                player.sendMessage("Это не ссылка!");
                return;
            }

            heroData.setValue(message);
            this.dataCommon.removeNewHero(player.getName());
            this.dataCommon.addPlayerHero(player.getName(), heroData);
            player.sendMessage("Вы успешно добавили скин!");
        }
    }
}
