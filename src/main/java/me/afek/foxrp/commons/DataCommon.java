package me.afek.foxrp.commons;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.objects.CharacterData;
import me.afek.foxrp.objects.TicketData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataCommon {

    ConcurrentHashMap<String, List<CharacterData>> characterPlayerData = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, CharacterData> enterNewCharacter = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Long> coolDownData = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, TicketData> ticketDataConcurrentHashMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Integer> warningsConcurrentHashMap = new ConcurrentHashMap<>();

    public void addPlayerWarning(String name) {
        name = name.toLowerCase();
        int warnings = this.warningsConcurrentHashMap.getOrDefault(name, 0);
        this.warningsConcurrentHashMap.put(name, ++warnings);
    }

    public void addPlayerWarning(String name, int warnings) {
        this.warningsConcurrentHashMap.put(name.toLowerCase(), warnings);
    }

    public boolean containPlayerWarning(String name) {
        return this.warningsConcurrentHashMap.containsKey(name.toLowerCase());
    }

    public int getPlayerWarnings(String name) {
        return this.warningsConcurrentHashMap.getOrDefault(name.toLowerCase(), 0);
    }

    public void addTicket(TicketData ticketData) {
        this.ticketDataConcurrentHashMap.put(ticketData.getIdTicket().toLowerCase(), ticketData);
    }

    public boolean containTicket(String ticketId) {
        return this.ticketDataConcurrentHashMap.containsKey(ticketId.toLowerCase());
    }

    public boolean containTicketByPlayer(String playerName) {
        playerName = playerName.toLowerCase();
        for (TicketData ticketData : this.ticketDataConcurrentHashMap.values())
            if (ticketData.getName().equalsIgnoreCase(playerName)) return true;

        return false;
    }

    public TicketData getTicket(String ticketId) {
        return this.ticketDataConcurrentHashMap.getOrDefault(ticketId.toLowerCase(), null);
    }

    public TicketData getTicketByPlayer(String playerName) {
        playerName = playerName.toLowerCase();
        for (TicketData ticketData : this.ticketDataConcurrentHashMap.values())
            if (ticketData.getName().equalsIgnoreCase(playerName)) return ticketData;

        return null;
    }

    public TicketData removeTicket(String ticketId) {
        return this.ticketDataConcurrentHashMap.remove(ticketId.toLowerCase());
    }

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
        List<CharacterData> playerCharacteres = this.characterPlayerData.getOrDefault(name.toLowerCase(), null);
        if (playerCharacteres == null)
            playerCharacteres = new ArrayList<>();

        playerCharacteres.add(CharacterData);
        this.characterPlayerData.put(name.toLowerCase(), playerCharacteres);
    }

    public void addPlayerCharacteres(String name, List<CharacterData> CharacterData) {
        this.characterPlayerData.put(name.toLowerCase(), CharacterData);
    }

    public List<CharacterData> getPlayerCharacteres(String name) {
        return this.characterPlayerData.getOrDefault(name.toLowerCase(), null);
    }

    public boolean containPlayer(String name) {
        return this.characterPlayerData.containsKey(name.toLowerCase());
    }

    public void clearAll() {
        this.characterPlayerData.clear();
        this.enterNewCharacter.clear();
        this.coolDownData.clear();
        this.ticketDataConcurrentHashMap.clear();
    }
}
