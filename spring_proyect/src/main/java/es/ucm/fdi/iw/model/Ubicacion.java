package es.ucm.fdi.iw.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity
@Data
@Table(name = "Coche")
public class Ubicacion {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="ID", unique=true, nullable=false)
    private int ID;

    @Column(name="direccion")
    private String direccion;

    @OneToMany(mappedBy="ubicacion")
	private List<Coche> coches;
}
