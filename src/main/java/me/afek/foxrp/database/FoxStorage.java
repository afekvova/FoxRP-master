package me.afek.foxrp.database;

import me.afek.foxrp.model.Ticket;

public interface FoxStorage {

    boolean connect();

    void disconnect();

    boolean isConnected();

    void saveTicket(final Ticket ticket);

    void saveWarning(final String playerName, final int warnings);

    void removeWarning(final String playerName);

    void removeTicket(final String ticketId);

}