package es.ucm.fdi.iw.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@Entity
@AllArgsConstructor
public class Ticket {
    
    @EmbeddedId
    private TicketID id;

    @Column (nullable = false)
    private LocalDate ocurranceDate;

    @Column (nullable = false)
    private String text;

    public enum Gravity {
        Bajo,
        Medio,
        Alto
    }

    @Column (nullable = false)
    private Gravity gravity;

}
