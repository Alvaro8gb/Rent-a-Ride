package es.ucm.fdi.iw.model;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.opencsv.CSVWriter;


@Data
@Entity
@NamedQueries({
    @NamedQuery(name="Ticket.deleteTicket",
            query="DELETE FROM Ticket t "
                    + "WHERE t.id = :id")
            })
@NamedQuery(name="Ticket.findAll",
            query="SELECT t FROM Ticket t ")
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


    public static String serializeToCSV(List<Ticket> tickets){
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);

        String[] header = {"id", "idUser", "idVehicle", "ocurranceDate", "text", "gravity"};
        csvWriter.writeNext(header);

        for (Ticket ticket : tickets) {
            String[] data = {
                String.valueOf(ticket.getId()),
                String.valueOf(ticket.getIdUser()),
                String.valueOf(ticket.getIdVehicle()),
                ticket.getOcurranceDate().toString(),
                ticket.getText(),
                ticket.getGravity().toString()
            };
            csvWriter.writeNext(data);
        }

        try {
            csvWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return stringWriter.toString();
    }
}
