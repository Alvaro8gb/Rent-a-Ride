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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Column;

import lombok.Data;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Data
@Entity
@NamedQueries({
        @NamedQuery(name="Booking.bydate", 
                query="SELECT b FROM Booking b "
                        + "WHERE (b.id.in_date = :in_date) or (b.id.out_date = :out_date)"),
        @NamedQuery(name="Booking.byUser", query="SELECT b FROM Booking b WHERE (b.id.userID = :userID)")
})
@AllArgsConstructor

public class Booking implements Transferable<Booking.Transfer> {
        
        @EmbeddedId
        private BookingID id;

        @Column(nullable = false)
        private Float price;

        @ManyToOne
        @MapsId("userID")
        private User user;

        @ManyToOne
        @MapsId("vehicleID")
        private Vehicle vehicle;

        public Booking() {
                
        }

        @Getter
        @AllArgsConstructor
        public static class Transfer {
                private BookingID.Transfer id;
                private Float price;
        }
        
        @Override
        public Transfer toTransfer() {
                return new Transfer(id.toTransfer(), price);
        }
        
        @Override
        public String toString() {
                return toTransfer().toString();
        }
}