package es.ucm.fdi.iw.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private String gravity;


}
