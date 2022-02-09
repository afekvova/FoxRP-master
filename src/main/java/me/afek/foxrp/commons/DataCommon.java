package me.afek.foxrp.commons;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.objects.HeroData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataCommon {

    ConcurrentHashMap<String, List<HeroData>> playerData = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, HeroData> enterNewHero = new ConcurrentHashMap<>();

    public void addNewHero(String name, HeroData data) {
        this.enterNewHero.put(name.toLowerCase(), data);
    }

    public HeroData getNewHero(String name) {
        return this.enterNewHero.getOrDefault(name.toLowerCase(), null);
    }

    public void removeNewHero(String name) {
        this.enterNewHero.remove(name.toLowerCase());
    }

    public void addPlayerHero(String name, HeroData heroData) {
        List<HeroData> playerHeroes = this.playerData.getOrDefault(name.toLowerCase(), null);
        if (playerHeroes == null)
            playerHeroes = new ArrayList<>();

        playerHeroes.add(heroData);
        this.playerData.put(name.toLowerCase(), playerHeroes);
    }

    public void addPlayerHeroes(String name, List<HeroData> heroData) {
        this.playerData.put(name.toLowerCase(), heroData);
    }

    public List<HeroData> getPlayerHeroes(String name) {
        return this.playerData.getOrDefault(name.toLowerCase(), null);
    }

    public boolean containPlayer(String name) {
        return this.playerData.containsKey(name.toLowerCase());
    }

    public void clearAll() {
        this.playerData.clear();
        this.enterNewHero.clear();
    }
}
