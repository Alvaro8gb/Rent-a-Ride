package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;


@Entity
@Data
@NamedQuery(name="Vehicle.byVechicle",
            query="SELECT v FROM Vehicle v "
                    + "WHERE v.vehicle LIKE CONCAT('%', :vehicle, '%')")

public class Vehicle {
   
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    private long id;

    @ManyToOne
    @JoinColumn(name="location")
	private Location location;

    @OneToMany(mappedBy = "vehicle")
    private List<Booking> bookings;

    @Column(nullable=false)
    private String vehicle;

    @Column(nullable=false)
    private String description;

    @Column(nullable=false)
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