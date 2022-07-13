package me.afek.foxrp.repositories.impl;

import me.afek.foxrp.model.Ticket;
import me.afek.foxrp.repositories.BaseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TicketRepository implements BaseRepository<String, Ticket> {

    private final ConcurrentHashMap<String, Ticket> tickets = new ConcurrentHashMap<>();

    @Override
    public void initial() {
        // Unsupported operation
    }

    @Override
    public void close() {
        this.tickets.clear();
    }

    @Override
    public void addData(String key, Ticket value) {
        this.tickets.put(value.getIdTicket().toLowerCase(), value);
    }

    @Override
    public boolean containData(String key) {
        return this.tickets.containsKey(key.toLowerCase());
    }

    @Override
    public Ticket removeData(String key) {
        return this.tickets.remove(key.toLowerCase());
    }

    @Override
    public Ticket getData(String key) {
        return this.tickets.getOrDefault(key.toLowerCase(), null);
    }

    @Override
    public List<Ticket> getData() {
        return new ArrayList<>(this.tickets.values());
    }

    public void removeAllTicket(List<String> tickets) {
        tickets.forEach(this::removeData);
    }
}
