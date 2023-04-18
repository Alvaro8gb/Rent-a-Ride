package es.ucm.fdi.iw.model;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Embeddable;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
public class BookingID implements Serializable, Transferable<BookingID.Transfer> {

    private long vehicleID;
    private long userID;

    private LocalDate in_date;
    private LocalDate out_date;

    public BookingID(){
        
    }
    @Getter
    @AllArgsConstructor
	public static class Transfer {
        private long vehicleID;
        private long userID;
        private String in_date;
        private String out_date;

    }
    @Override
    public Transfer toTransfer() {
       return new Transfer(vehicleID, userID, 
       DateTimeFormatter.ISO_LOCAL_DATE.format(in_date), 
       DateTimeFormatter.ISO_LOCAL_DATE.format(out_date));
    }

}