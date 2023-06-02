package es.ucm.fdi.iw.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Data;
import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * A message that users can send each other.
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Message.findAllUnattended",
	query="SELECT m FROM Message m WHERE m.unattended = true")
			,
			
	@NamedQuery(name="Message.countUnread",
	query="SELECT COUNT(m) FROM Message m "
			+ "WHERE m.recipient.id = :userId AND m.dateRead = null")
			,
	
	@NamedQuery(name="Message.chat",
	query="SELECT m FROM Message m "
			+ "WHERE m.dateRead = null and ((m.recipient.id = :idReceptor and m.sender.id = :idSender) or"
			+ " (m.recipient.id = :idSender and m.sender.id = :idReceptor)) "
			+ "ORDER BY m.dateSent"),
	
	@NamedQuery(name="Message.findAllByUserID",
	query="SELECT m FROM Message m "
			+ "WHERE m.sender.id = :idUser or m.recipient.id = :idUser"),

	@NamedQuery(name = "Message.findAllAttended",
	query = "SELECT m FROM Message m JOIN User u ON (u.id = m.sender.id)" +
			"WHERE m.unattended = false AND u.roles NOT LIKE '%GESTOR%' AND u.roles NOT LIKE '%ADMIN%'" +
			"AND m.dateSent = (" +
			"    SELECT MAX(m2.dateSent) FROM Message m2 " +
			"    WHERE m2.sender = m.sender " +
			"    AND m2.recipient = m.recipient)"
	)
})


@Data
public class Message implements Transferable<Message.Transfer> {
	
	private static Logger log = LogManager.getLogger(Message.class);	
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
	private long id;
	@ManyToOne
	private User sender;
	@ManyToOne
	private User recipient;
	private String text;
	private boolean unattended;
	
	private LocalDateTime dateSent;
	private LocalDateTime dateRead;
	
	/**
	 * Objeto para persistir a/de JSON
	 * @author mfreire
	 */
    @Getter
    @AllArgsConstructor
	public static class Transfer {
		private String from;
		private String to;
		private String sent;
		private String received;
		private String text;
		private long senderID;
		private long recipientID;
		long id;
		public Transfer(Message m) {
			this.from = m.getSender().getUsername();
			this.to = m.getRecipient().getUsername();
			this.sent = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(m.getDateSent());
			this.received = m.getDateRead() == null ?
					null : DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(m.getDateRead());
			this.text = m.getText();
			this.senderID = m.getSender().getId();
			this.recipientID = m.getRecipient().getId();
			this.id = m.getId();
		}
	}

	@Override
	public Transfer toTransfer() {

		return new Transfer(sender.getUsername(), 
			recipient == null ? null : recipient.getUsername(), 
			DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateSent),
			dateRead == null ? null : DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateRead),
			text, sender.getId(), recipient == null ? -1 : recipient.getId(), id
        );
    }
}
