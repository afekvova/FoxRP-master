package me.afek.foxrp.services;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.FormatUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Locale;

@RequiredArgsConstructor
public class EssentialsService {

    private final Essentials essentials;

    public boolean setPlayerName(Player player, String name) {
        return this.setPlayerName(this.essentials.getUser(player), name);
    }

    public boolean setPlayerName(User target, String name) {
        if (this.nickInUse(target, name))
            return false;

        this.setNickName(target, (name.isEmpty() || "off".equalsIgnoreCase(name)) ? null : name);
        return true;
    }

    private boolean nickInUse(final User target, final String nick) {
        final String lowerNick = FormatUtil.stripFormat(nick.toLowerCase(Locale.ENGLISH));
        for (final User onlinePlayer : this.essentials.getOnlineUsers()) {
            if (target.getBase().getName().equals(onlinePlayer.getName())) {
                continue;
            }

            final String matchNick = FormatUtil.stripFormat(onlinePlayer.getNickname());
            if ((matchNick != null && !matchNick.isEmpty() && lowerNick.equals(matchNick.toLowerCase(Locale.ENGLISH))) || lowerNick.equals(onlinePlayer.getName().toLowerCase(Locale.ENGLISH))) {
                return true;
            }
        }
        final User fetchedUser = this.essentials.getUser(lowerNick);
        return fetchedUser != null && fetchedUser != target;
    }

    private void setNickName(User player, String name) {
        player.setNickname(name);
        player.setDisplayNick();
    }
}
