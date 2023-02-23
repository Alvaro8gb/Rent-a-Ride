package es.ucm.fdi.iw.model;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Column;

import lombok.Data;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Data
@Entity
@Table(name = "Booking")
public class Booking implements Transferable<Booking.Transfer> {

        @EmbeddedId
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private BookingID id;

        @Column(nullable = false)
        private Date in;
        
        private Date out;

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
                private Date in;
                private Date out;
                private Float priceByDay;
        }
        
        @Override
        public Transfer toTransfer() {
                return new Transfer(id,	in, out, priceByDay);
        }
            
        @Override
        public String toString() {
                return toTransfer().toString();
        }

}