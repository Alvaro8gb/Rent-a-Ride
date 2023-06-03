package es.ucm.fdi.iw.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Column;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@NamedQueries({
        @NamedQuery(name="Booking.bydate", 
                query="SELECT b FROM Booking b "
                        + "WHERE (b.in_date = :in_date) or (b.out_date = :out_date)"),
        @NamedQuery(name="Booking.byUser", query="SELECT b FROM Booking b WHERE (b.user.id = :userID)"),
        @NamedQuery(name="Booking.byId", query="SELECT b FROM Booking b "+ "WHERE b.idr = :id"),
        @NamedQuery(name="Booking.findAll", query="SELECT b FROM Booking b ")
})
@AllArgsConstructor
@NoArgsConstructor
public class Booking implements Transferable<Booking.Transfer> {
        
        @Id
        private String idr;
        
        @Column(nullable = false)
        private LocalDate in_date;

        @Column(nullable = false)
        private LocalDate out_date;

        @Column(nullable = false)
        private Float price;

        @ManyToOne
        private User user;

        @ManyToOne
        private Vehicle vehicle;

        @Column(nullable = false)
        private boolean cancelled;

        @Getter
        @AllArgsConstructor
        public static class Transfer {
                private long vehicleID;
                private long userID;
                private String in_date;
                private String out_date;
                private float price;
        }
        
        @Override
        public Transfer toTransfer() {
                return new Transfer(vehicle.getId(), user.getId(), 
                DateTimeFormatter.ISO_LOCAL_DATE.format(in_date), 
                DateTimeFormatter.ISO_LOCAL_DATE.format(out_date),
                price);
        }
        
        @Override
        public String toString() {
                return toTransfer().toString();
        }
}