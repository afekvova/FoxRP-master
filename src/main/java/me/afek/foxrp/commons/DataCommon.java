package me.afek.foxrp.commons;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.objects.CharacterData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataCommon {

    ConcurrentHashMap<String, List<CharacterData>> playerData = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, CharacterData> enterNewCharacter = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Long> coolDownData = new ConcurrentHashMap<>();

    public void addCoolDown(String name, long coolDown) {
        this.coolDownData.put(name.toLowerCase(), coolDown);
    }

    public long getCoolDown(String name) {
        return this.coolDownData.getOrDefault(name.toLowerCase(), System.currentTimeMillis());
    }

    public boolean containCoolDown(String name) {
        return this.coolDownData.containsKey(name.toLowerCase());
    }

    public void removeCoolDown(String name) {
        this.coolDownData.remove(name.toLowerCase());
    }

    public void addNewCharacter(String name, CharacterData data) {
        this.enterNewCharacter.put(name.toLowerCase(), data);
    }

    public CharacterData getNewCharacter(String name) {
        return this.enterNewCharacter.getOrDefault(name.toLowerCase(), null);
    }

    public void removeNewCharacter(String name) {
        this.enterNewCharacter.remove(name.toLowerCase());
    }

    public void addPlayerCharacter(String name, CharacterData CharacterData) {
        List<CharacterData> playerCharacteres = this.playerData.getOrDefault(name.toLowerCase(), null);
        if (playerCharacteres == null)
            playerCharacteres = new ArrayList<>();

        playerCharacteres.add(CharacterData);
        this.playerData.put(name.toLowerCase(), playerCharacteres);
    }

    public void addPlayerCharacteres(String name, List<CharacterData> CharacterData) {
        this.playerData.put(name.toLowerCase(), CharacterData);
    }

    public List<CharacterData> getPlayerCharacteres(String name) {
        return this.playerData.getOrDefault(name.toLowerCase(), null);
    }

    public boolean containPlayer(String name) {
        return this.playerData.containsKey(name.toLowerCase());
    }

    public void clearAll() {
        this.playerData.clear();
        this.enterNewCharacter.clear();
        this.coolDownData.clear();
    }
}
