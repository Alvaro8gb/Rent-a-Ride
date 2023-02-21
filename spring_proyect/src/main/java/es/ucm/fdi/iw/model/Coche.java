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
@Table(name = "Coche")
public class Coche {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="ID", unique=true, nullable=false)
    private int ID;

    @ManyToOne
	@JoinColumn(name="IDUbicacion")
	private Ubicacion ubicacion;

    /*
    @OneToMany(mappedBy="coche")
    private List<Booking> reservas;
     */
    
    @Column(name = "modelo", nullable=false)
    private String modelo;

    @Column(name = "descripcion", nullable=false)
    private String descripcion;

    @Column(name = "anio", nullable=false)
    private int anio;

    public enum Combustible{
        Gasolina,
        Diesel,
        Electrico,
        Hibrido,
        Gas
    }
    @Enumerated(EnumType.STRING)
    @Column(name = "combustible", nullable = false)
    private Combustible combustible;

    @Column(name = "consumoCiudad", nullable = false)
    private float consumoCiudad;

    @Column(name = "consumoCarretera", nullable = false)
    private float consumoCarretera;

    public enum Cambio{
        Automatico,
        Manual
    }
    @Enumerated(EnumType.STRING)
    @Column(name = "cambio", nullable = false)
    private Combustible cambio;

    @Column(name = "puertas", nullable = false)
    private int puertas;

    @Column(name = "plazas", nullable = false)
    private int plazas;

    @Column(name = "automonia", nullable = false)
    private int automonia;

}
