package me.afek.foxrp.commons;

import me.afek.foxrp.objects.HeroData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DataCommon {

    private final ConcurrentHashMap<String, List<HeroData>> playerData;

    public DataCommon() {
        this.playerData = new ConcurrentHashMap<>();
    }

    public void addPlayerHero(String name, HeroData heroData) {
        List<HeroData> playerHeroes = this.playerData.getOrDefault(name.toLowerCase(), null);
        if (playerHeroes == null)
            playerHeroes = new ArrayList<>();

        playerHeroes.add(heroData);
        this.playerData.put(name.toLowerCase(), playerHeroes);
    }

    public List<HeroData> getPlayerHeroes(String name) {
        return this.playerData.getOrDefault(name.toLowerCase(), null);
    }

    public boolean containPlayer(String name) {
        return this.playerData.containsKey(name.toLowerCase());
    }

    public void clearAll() {
        this.playerData.clear();
    }
}
