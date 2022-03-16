package me.afek.foxrp.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.database.Sql;
import me.afek.foxrp.objects.TicketData;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerTicketsTask implements Runnable {

    DataCommon dataCommon;
    Sql sql;

    @Override
    public void run() {
        List<String> removeTicketList = new ArrayList<>();
        for (TicketData ticketData : this.dataCommon.getTicketDataConcurrentHashMap().values())
            if (ticketData.getFinalTime() <= System.currentTimeMillis()) {
                removeTicketList.add(ticketData.getIdTicket());
                this.sql.removeTicket(ticketData.getIdTicket());

                String playerName = ticketData.getName();
                this.dataCommon.addPlayerWarning(playerName);
                this.sql.saveWarning(playerName, this.dataCommon.getPlayerWarnings(playerName));
            }

        if (!removeTicketList.isEmpty())
            this.dataCommon.removeAllTicket(removeTicketList);
    }
}
