package me.afek.foxrp.tasks;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.afek.foxrp.database.FoxStorage;
import me.afek.foxrp.model.Ticket;
import me.afek.foxrp.repositories.impl.TicketRepository;
import me.afek.foxrp.repositories.impl.WarningRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerTicketsTask implements Runnable {

    TicketRepository ticketRepository;
    WarningRepository warningRepository;
    FoxStorage foxStorage;

    @Override
    public void run() {
        List<String> removeTicketList = new ArrayList<>();
        long currentTime = System.currentTimeMillis();

        for (Ticket ticketData : this.ticketRepository.getData())
            if (ticketData.getFinalTime() <= currentTime) {
                removeTicketList.add(ticketData.getIdTicket());
                this.foxStorage.removeTicket(ticketData.getIdTicket());

                String playerName = ticketData.getName();
                this.warningRepository.addData(playerName, 1);
                this.foxStorage.saveWarning(playerName, this.warningRepository.getData(playerName));
            }

        if (!removeTicketList.isEmpty())
            this.ticketRepository.removeAllTicket(removeTicketList);
    }
}
