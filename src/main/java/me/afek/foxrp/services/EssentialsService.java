package me.afek.foxrp.services;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.FormatUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Locale;

@RequiredArgsConstructor
public class EssentialsService {

    private final Essentials essentials;

    public boolean setPlayerName(Player player, String name) {
        return this.setPlayerName(this.essentials.getUser(player), name);
    }

    public boolean setPlayerName(User target, String name) {
        if (name == null || name.isEmpty() || "off".equalsIgnoreCase(name)) {
            this.setNickname(target, null);
        } else if (target.getName().equalsIgnoreCase(name)) {
            this.setNickname(target, name);
        } else {
            if (this.nickInUse(target, name))
                return false;

            this.setNickname(target, name);
        }

        return true;
    }

    private boolean nickInUse(final User target, final String nick) {
        String lowerNick = FormatUtil.stripFormat(nick.toLowerCase(Locale.ENGLISH));
        Iterator var4 = this.essentials.getOnlineUsers().iterator();

        User onlinePlayer;
        String matchNick;
        do {
            do {
                if (!var4.hasNext()) {
                    User fetchedUser = this.essentials.getUser(lowerNick);
                    return fetchedUser != null && fetchedUser != target;
                }

                onlinePlayer = (User) var4.next();
            } while (target.getBase().getName().equals(onlinePlayer.getName()));

            matchNick = FormatUtil.stripFormat(onlinePlayer.getNickname());
        } while ((matchNick == null || matchNick.isEmpty() || !lowerNick.equals(matchNick.toLowerCase(Locale.ENGLISH))) && !lowerNick.equals(onlinePlayer.getName().toLowerCase(Locale.ENGLISH)));

        return true;
    }

    private void setNickname(User player, String name) {
        player.setNickname(name);
        player.setDisplayNick();
    }
}
