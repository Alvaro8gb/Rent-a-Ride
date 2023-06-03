package es.ucm.fdi.iw.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@NamedQueries({
        @NamedQuery(name = "Vehicle.byVechicle", query = "SELECT v FROM Vehicle v "
                + "JOIN Location l ON (l.id = v.location)"
                + "WHERE UPPER(v.modelName) LIKE CONCAT('%', UPPER(:modelName), '%') AND l.name=:location"),

        @NamedQuery(name = "Vehicle.findAll", query = "SELECT v FROM Vehicle v "),
        @NamedQuery(name = "Vehicle.allLocation", query = "SELECT l.name FROM Location l"),
        @NamedQuery(name = "Vehicle.searchWithFilter", query = "SELECT v FROM Vehicle v WHERE UPPER(CONCAT(v.brand, ' ' ,v.modelName)) LIKE CONCAT('%', UPPER(:filtro), '%')")
})

@NoArgsConstructor
public class Vehicle implements Transferable<Vehicle.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "location")
    private Location location;

    @OneToMany(mappedBy = "vehicle")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "vehicle")
    private List<Ticket> incidencias;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    private int oldYear;

    public enum Fuel {
        Gasolina,
        Diesel,
        Electrico,
        Hibrido,
        Gas
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Fuel fuel;

    @Column(nullable = false)
    private float consumption;

    public enum Transmission {
        Automatico,
        Manual
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Transmission transmission;

    @Column(nullable = false)
    private int doors;

    @Column(nullable = false)
    private int seats;

    @Column(nullable = false)
    private int cv;

    @Column(nullable = false)
    private String license;

    @Column(nullable = false)
    private int autonomy;

    @Column(nullable = false)
    private float priceByDay;

    public boolean isAvailable() {
        LocalDate current_date = LocalDate.now();

        for (Booking b : bookings) {
            if (!(current_date.isAfter(b.getIn_date()) && current_date.isBefore(b.getOut_date()))) {
                return false;
            }
        }

        return true;
    }

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private String brand;
        private String modelName;
        private int oldYear;
        private Fuel fuel;
        private float consumption;
        private Transmission transmission;
        private int doors;
        private int seats;
        private int cv;
        private String license;
        private int autonomy;
        private float priceByDay;

    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, brand, modelName, oldYear,
                fuel, consumption, transmission, doors, seats,
                cv, license, autonomy, priceByDay);
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }

}