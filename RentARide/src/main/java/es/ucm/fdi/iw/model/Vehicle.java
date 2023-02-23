package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.List;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

@Entity
@Data
@Table(name = "Vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    private long id;

    @ManyToOne
	private Location location;

    @OneToMany(mappedBy = "vehicle")
    private List<Booking> bookings;

    @Column(nullable=false)
    private String model;

    @Column(nullable=false)
    private String description;

    @Column(nullable=false)
    private int year;

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
    private float cityConsumption;

    @Column(nullable = false)
    private float roadConsumption;

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
    private int autonomy;

}