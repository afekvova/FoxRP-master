package me.afek.foxrp.repositories.impl;

import me.afek.foxrp.model.Ticket;
import me.afek.foxrp.repositories.BaseRepository;
import me.afek.foxrp.repositories.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TicketRepository implements BaseRepository<String, Ticket> {

    private final Map<String, Ticket> tickets = new ConcurrentHashMap<>();

    @Override
    public void initial() {
        throw new UnsupportedOperationException();
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
    public boolean containsData(String key) {
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
