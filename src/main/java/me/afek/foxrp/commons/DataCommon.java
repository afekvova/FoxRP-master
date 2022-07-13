package me.afek.foxrp.commons;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.model.Character;
import me.afek.foxrp.model.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataCommon {

    ConcurrentHashMap<String, List<Character>> characterPlayerData = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Character> enterNewCharacter = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Ticket> ticketDataConcurrentHashMap = new ConcurrentHashMap<>();
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

    public void addTicket(Ticket ticketData) {
        this.ticketDataConcurrentHashMap.put(ticketData.getIdTicket(), ticketData);
    }

    public boolean containTicket(String ticketId) {
        return this.ticketDataConcurrentHashMap.containsKey(ticketId);
    }

    public boolean containTicketByPlayer(String playerName) {
        playerName = playerName.toLowerCase();
        for (Ticket ticketData : this.ticketDataConcurrentHashMap.values())
            if (ticketData.getName().equalsIgnoreCase(playerName)) return true;

        return false;
    }

    public Ticket getTicket(String ticketId) {
        return this.ticketDataConcurrentHashMap.getOrDefault(ticketId, null);
    }

    public Ticket getTicketByPlayer(String playerName) {
        playerName = playerName.toLowerCase();
        for (Ticket ticketData : this.ticketDataConcurrentHashMap.values())
            if (ticketData.getName().equalsIgnoreCase(playerName)) return ticketData;

        return null;
    }

    public List<Ticket> getTicketsByPlayer(String playerName) {
        List<Ticket> tickets = new ArrayList<>();

        playerName = playerName.toLowerCase();
        for (Ticket ticketData : this.ticketDataConcurrentHashMap.values())
            if (ticketData.getName().equalsIgnoreCase(playerName)) tickets.add(ticketData);

        return tickets;
    }

    public Ticket removeTicket(String ticketId) {
        return this.ticketDataConcurrentHashMap.remove(ticketId);
    }

    public void removeAllTicket(List<String> tickets) {
        tickets.forEach(this::removeTicket);
    }

    public void addNewCharacter(String name, Character data) {
        this.enterNewCharacter.put(name.toLowerCase(), data);
    }

    public Character getNewCharacter(String name) {
        return this.enterNewCharacter.getOrDefault(name.toLowerCase(), null);
    }

    public void removeNewCharacter(String name) {
        this.enterNewCharacter.remove(name.toLowerCase());
    }

    public void addPlayerCharacter(String name, Character CharacterData) {
        List<Character> playerCharacteres = this.characterPlayerData.getOrDefault(name.toLowerCase(), null);
        if (playerCharacteres == null)
            playerCharacteres = new ArrayList<>();

        playerCharacteres.add(CharacterData);
        this.characterPlayerData.put(name.toLowerCase(), playerCharacteres);
    }

    public void addPlayerCharacteres(String name, List<Character> CharacterData) {
        this.characterPlayerData.put(name.toLowerCase(), CharacterData);
    }

    public List<Character> getPlayerCharacteres(String name) {
        return this.characterPlayerData.getOrDefault(name.toLowerCase(), null);
    }

    public boolean containPlayer(String name) {
        return this.characterPlayerData.containsKey(name.toLowerCase());
    }

    public void clearAll() {
        this.characterPlayerData.clear();
        this.enterNewCharacter.clear();
        this.ticketDataConcurrentHashMap.clear();
    }
}
