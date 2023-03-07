package es.ucm.fdi.iw.model;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Embeddable;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
public class TicketID implements Serializable {
    
    private BookingID bookingID;
}
