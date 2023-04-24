package es.ucm.fdi.iw.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.NamedQueries;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.OneToMany;

import lombok.Data;

@NamedQueries({
    @NamedQuery(name="Location.byName",
    query="SELECT l FROM Location l WHERE l.name = :name"),
    @NamedQuery(name="Location.findAll",
    query="SELECT l FROM Location l ")
})

@Entity
@Data
public class Location {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int contactNumber;

    @OneToMany(mappedBy="location")
	private List<Vehicle> vehicles;
    
}