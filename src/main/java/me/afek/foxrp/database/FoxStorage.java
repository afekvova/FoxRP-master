package me.afek.foxrp.database;

import me.afek.foxrp.model.Ticket;

public interface FoxStorage {

    public boolean connect();

    public void disconnect();

    public boolean isConnected();

    public void saveTicket(Ticket ticket);

    public void saveWarning(String playerName, int warnings);

    public void removeWarning(String playerName);

    public void removeTicket(String ticketId);
}