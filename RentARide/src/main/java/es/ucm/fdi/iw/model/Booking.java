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
                private Float priceByDay;
        }
        
        @Override
        public Transfer toTransfer() {
                return new Transfer(id, priceByDay);
        }
            
        @Override
        public String toString() {
                return toTransfer().toString();
        }
}