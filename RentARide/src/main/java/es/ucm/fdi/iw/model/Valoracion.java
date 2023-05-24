package es.ucm.fdi.iw.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Entity
@NamedQueries({
    @NamedQuery(name="Valoracion.getAll", 
            query="SELECT v FROM Valoracion v")
})
@AllArgsConstructor
public class Valoracion implements Transferable<Valoracion.Transfer>{
    
    @EmbeddedId
    private ValoracionID id;

    @Column(nullable = false)
        private int rating;

    @Column(nullable = false)
    private String texto;

    @Column(nullable = false)
    private LocalDate in_date;
    
    @ManyToOne
    @MapsId("userID")
    private User user;

    @ManyToOne
    @MapsId("vehicleID")
    private Vehicle vehicle;

    public Valoracion(){
        
    }
    @Getter
    @AllArgsConstructor
    public static class Transfer {
            private ValoracionID.Transfer id;
            private String texto;
            private int rating;
            private LocalDate in_date;
    }
    
    @Override
    public Transfer toTransfer() {
            return new Transfer(id.toTransfer(), texto,rating,in_date);
    }
    
    @Override
    public String toString() {
            return toTransfer().toString();
    }


}
