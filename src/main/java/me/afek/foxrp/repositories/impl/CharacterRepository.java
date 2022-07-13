package me.afek.foxrp.repositories.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.model.Character;
import me.afek.foxrp.repositories.BaseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CharacterRepository implements BaseRepository<String, Character> {

    @Getter
    ConcurrentHashMap<String, List<Character>> characterPlayerData = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Character> enterNewCharacter = new ConcurrentHashMap<>();

    public void addPlayerCharacter(String name, Character CharacterData) {
        List<Character> playerCharacteres = this.characterPlayerData.getOrDefault(name.toLowerCase(), null);
        if (playerCharacteres == null)
            playerCharacteres = new ArrayList<>();

        playerCharacteres.add(CharacterData);
        this.characterPlayerData.put(name.toLowerCase(), playerCharacteres);
    }

    public void addPlayerCharacters(String name, List<Character> CharacterData) {
        this.characterPlayerData.put(name.toLowerCase(), CharacterData);
    }

    public List<Character> getPlayerCharacters(String name) {
        return this.characterPlayerData.getOrDefault(name.toLowerCase(), null);
    }

    public boolean containPlayer(String name) {
        return this.characterPlayerData.containsKey(name.toLowerCase());
    }

    @Override
    public void initial() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        this.characterPlayerData.clear();
        this.enterNewCharacter.clear();
    }

    @Override
    public void addData(String key, Character value) {
        this.enterNewCharacter.put(key.toLowerCase(), value);
    }

    @Override
    public boolean containData(String key) {
        return this.enterNewCharacter.containsKey(key.toLowerCase());
    }

    @Override
    public Character removeData(String key) {
        return this.enterNewCharacter.remove(key.toLowerCase());
    }

    @Override
    public Character getData(String key) {
        return this.enterNewCharacter.getOrDefault(key.toLowerCase(), null);
    }

    @Override
    public List<Character> getData() {
        return new ArrayList<>(this.enterNewCharacter.values());
    }


}
