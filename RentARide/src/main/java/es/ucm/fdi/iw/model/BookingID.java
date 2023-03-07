package es.ucm.fdi.iw.model;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Embeddable;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
public class BookingID implements Serializable {

    private long vehicleID;
    private long userID;

    private LocalDate in_date;
    
    private LocalDate out_date;

}