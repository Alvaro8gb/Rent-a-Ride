package es.ucm.fdi.iw.model;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
public class TicketID implements Serializable {
    
    private BookingID bookingID;
    
    /*
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id; 

    */
}
