package me.afek.foxrp.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.commons.DataCommon;
import me.afek.foxrp.database.Sql;
import me.afek.foxrp.objects.TicketData;

import java.util.Iterator;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerTicketsTask implements Runnable {

    DataCommon dataCommon;
    Sql sql;

    @Override
    public void run() {
        Iterator<TicketData> ticketDataIterator = this.dataCommon.getTicketDataConcurrentHashMap().values().iterator();
        if (ticketDataIterator.hasNext()) {
            TicketData ticketData = ticketDataIterator.next();
            if (ticketData.getFinalTime() <= System.currentTimeMillis()) {
                ticketDataIterator.remove();
                this.sql.removeTicket(ticketData.getIdTicket());
                this.dataCommon.addPlayerWarning(ticketData.getName());
            }
        }
    }
}
