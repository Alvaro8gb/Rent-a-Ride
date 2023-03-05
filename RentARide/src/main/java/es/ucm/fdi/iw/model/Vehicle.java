package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.List;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.GeneratedValue;
import javax.persistence.NamedQueries;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;


@Entity
@Data
@NamedQueries({
    @NamedQuery(name="Vehicle.byVechicle",
            query="SELECT v FROM Vehicle v "
                    + "WHERE UPPER(v.vehicle) LIKE CONCAT('%', UPPER(:vehicle), '%')"),
            
    @NamedQuery(name="Vehicle.findAll",
            query="SELECT v FROM Vehicle v ")
})

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

    @Column(nullable = false)
    private String imagePath;

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append(String.format("Vehicle: %s\n", vehicle));
        strBuilder.append(String.format("description: %s\n", description));
        strBuilder.append(String.format("oldYear: %s\n", oldYear));
        strBuilder.append(String.format("fuel: %s\n", fuel));
        strBuilder.append(String.format("cityConsumption: %f\n", cityConsumption));
        strBuilder.append(String.format("roadConsumption: %f\n", roadConsumption));
        strBuilder.append(String.format("transmission: %s\n", transmission));
        strBuilder.append(String.format("doors: %d\n", doors));
        strBuilder.append(String.format("seats: %d\n", seats));
        strBuilder.append(String.format("autonomy: %d\n", autonomy));
        strBuilder.append(String.format("imagePath: %s\n", imagePath));

        return strBuilder.toString();
    }

}