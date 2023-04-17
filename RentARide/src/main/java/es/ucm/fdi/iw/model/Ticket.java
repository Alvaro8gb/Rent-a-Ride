package es.ucm.fdi.iw.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@Entity
@AllArgsConstructor
public class Ticket {
    
    public Ticket() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
	private long id;

    @Column (nullable= false)
    private long idUser;
    
    @Column (nullable = false)
    private long idVehicle;

    @Column (nullable = false)
    private LocalDate ocurranceDate;
    
    @Column (nullable = false)
    private String text;

    public enum Gravity {
        Baja,
        Media,
        Alta
    }

    @Column (nullable = false)
    private Gravity gravity;

}
