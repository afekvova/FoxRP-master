package me.afek.foxrp.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.database.FoxStorage;
import me.afek.foxrp.model.Ticket;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerTicketsTask implements Runnable {

    DataCommon dataCommon;
    FoxStorage foxStorage;

    @Override
    public void run() {
        List<String> removeTicketList = new ArrayList<>();
        for (Ticket ticketData : this.dataCommon.getTicketDataConcurrentHashMap().values())
            if (ticketData.getFinalTime() <= System.currentTimeMillis()) {
                removeTicketList.add(ticketData.getIdTicket());
                this.foxStorage.removeTicket(ticketData.getIdTicket());

                String playerName = ticketData.getName();
                this.dataCommon.addPlayerWarning(playerName);
                this.foxStorage.saveWarning(playerName, this.dataCommon.getPlayerWarnings(playerName));
            }

        if (!removeTicketList.isEmpty())
            this.dataCommon.removeAllTicket(removeTicketList);
    }
}
