package me.afek.foxrp.objects;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class TicketData {

    String idTicket, name, reason;
    int diamonds;
    long finalTime;
}
