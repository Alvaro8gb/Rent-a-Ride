package es.ucm.fdi.iw.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Column;

import lombok.Data;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Data
@Entity
@AllArgsConstructor
public class Booking implements Transferable<Booking.Transfer> {
        
        @EmbeddedId
        private BookingID id;

        @Column(nullable = false)
        private LocalDate in_date;
        
        private LocalDate out_date;

        @Column(nullable = false)
        private Float priceByDay;

        @ManyToOne
        @MapsId("userID")
        private User user;

        @ManyToOne
        @MapsId("vehicleID")
        private Vehicle vehicle;

        @Getter
        @AllArgsConstructor
        public static class Transfer {
                private BookingID id;
                private String in_date;
                private String out_date;
                private Float priceByDay;
        }
        
        @Override
        public Transfer toTransfer() {
                return new Transfer(id,	
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(in_date), 
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(out_date), 
                priceByDay);
        }
            
        @Override
        public String toString() {
                return toTransfer().toString();
        }
}