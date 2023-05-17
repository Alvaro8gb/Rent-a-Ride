package es.ucm.fdi.iw.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.time.LocalDateTime;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.Transferable;
import es.ucm.fdi.iw.model.User;

import com.fasterxml.jackson.annotation.ObjectIdGenerators.StringIdGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * User-administration controller
 * 
 * @author mfreire
 */
@Controller()
@RequestMapping("messages")
public class MessageController {

	private static final Logger log = LogManager.getLogger(MessageController.class);

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	// private List<Message> pendingMsgs;
	// private User bot = new User();

	public MessageController() {
		// pendingMsgs = new ArrayList<>();

		// bot.setUsername("Rent a Ride Bot");
	}
	
	@GetMapping("/in")
	public String inChats(Model model) {
		List<Message> pendingMsgs = entityManager.createNamedQuery("Message.findAllUnattended", Message.class).getResultList();
		List<Message> attendedMsgs = entityManager.createNamedQuery("Message.findAllAttended", Message.class).getResultList();

		model.addAttribute("pendingMsgs", pendingMsgs); // Return messages queue
		model.addAttribute("attendedMsgs", attendedMsgs); // Return attended history

		return "messages";
	}

	/**
	 * Send message through Websockect to user
	 * 
	 * @param msg
	 * @param target user
	 * @throws JsonProcessingException
	 */
	private void sendMsg(Message msg, User u) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();

		String json = mapper.writeValueAsString(msg.toTransfer());

		log.info("Sending a message to {} with contents '{}'", u.getId(), json);

		messagingTemplate.convertAndSend("/user/" + u.getUsername() + "/queue/updates", json);
	}

	/**
	 * Posts a message to a user.
	 * 
	 * @param id of target user (source user is from ID)
	 * @param o  JSON-ized message, similar to {"message": "text goes here"}
	 * @throws JsonProcessingException
	 */
	@PostMapping(path = "/user/{idUser}")
	@ResponseBody
	@Transactional
	public String postMsg(@PathVariable long idUser,
							@RequestBody JsonNode o, Model model, HttpSession session)
							throws JsonProcessingException {

		Message message = new Message();
		User sender = entityManager.find(User.class, ((User) session.getAttribute("u")).getId());
		
		message.setDateSent(LocalDateTime.now());
		message.setText(o.get("message").asText());
		message.setSender(sender);
		
		if (o.get("firstMessage").asBoolean(false)) {
			message.setUnattended(true);
			message.setRecipient(null);
			
			entityManager.persist(message);
			entityManager.flush();
		} else {
			User recipient = entityManager.find(User.class, idUser);
			message.setRecipient(recipient);
			
			entityManager.persist(message);
			entityManager.flush();

			sendMsg(message, recipient); // Enviamos
		}

		return "{\"result\": \"message sent.\"}";
	}

	/**
	 * Posts a message to a user.
	 * 
	 * @param id of target user (source user is from ID)
	 * @param o  JSON-ized message, similar to {"message": "text goes here"}
	 * @throws JsonProcessingException
	 */
	@PostMapping(path = "/{idMessage}", produces = "application/json")
	@Transactional
	@ResponseBody
	public String acceptMsg(@PathVariable long idMessage, Model model, HttpSession session) throws JsonProcessingException {
		Message message = entityManager.find(Message.class, idMessage);
		User recevier = entityManager.find(User.class, ((User) session.getAttribute("u")).getId());

		if (message != null) {
			if (message.getRecipient() != null)
				return "{\"result\": \"error\", \"data\": \"chat already attended\"}";

			message.setRecipient(recevier);
			message.setUnattended(false);
			entityManager.persist(message);
			entityManager.flush();

			sendMsg(message, recevier);

			// Notify user the agent acceptance
			Message confirm = new Message();
			confirm.setDateSent(LocalDateTime.now());
			confirm.setSender(recevier);
			confirm.setRecipient(message.getSender());
			confirm.setText("chatAccepted");

			entityManager.persist(confirm);
			entityManager.flush();

			sendMsg(confirm, message.getSender());

			StringBuilder stringBuilder = new StringBuilder();

			stringBuilder.append("{ \"result\": \"accepted\", ");
			stringBuilder.append(String.format("\"clientName\": \"%s\"", 
									message.getSender().getFirstName() + ' ' + message.getSender().getLastName()));
			stringBuilder.append("}");

			return stringBuilder.toString();
		}
		
		return "{\"result\": \"error\"}";
	}

	@GetMapping(path = "/receivedfrom/{idSender}", produces = "application/json")
	@Transactional // para no recibir resultados inconsistentes
	@ResponseBody // para indicar que no devuelve vista, sino un objeto (jsonizado)
	public String chatMessages(@PathVariable long idSender, HttpSession session) throws JsonProcessingException {
		User receptor = entityManager.find(User.class, ((User) session.getAttribute("u")).getId());

		List<Message> msgs = entityManager.createNamedQuery("Message.chat", Message.class)
				.setParameter("idReceptor", receptor.getId())
				.setParameter("idSender", idSender)
				.getResultList();


		ObjectMapper objectMapper = new ObjectMapper();
		String msgsJson = objectMapper.writeValueAsString(msgs.stream().map(Message::toTransfer).collect(Collectors.toList()));		

		log.info("Getting messages between {} and {} --> ", idSender, receptor.getId(), msgsJson);

		return "{\"msgs\": " + msgsJson + "}";
	}


	@GetMapping(path = "/unread", produces = "application/json")
	@ResponseBody
	public String checkUnread(HttpSession session) {
		long userId = ((User) session.getAttribute("u")).getId();
		long unread = entityManager.createNamedQuery("Message.countUnread", Long.class)
				.setParameter("userId", userId)
				.getSingleResult();
		session.setAttribute("unread", unread);
		return "{\"unread\": " + unread + "}";
	}

	/**
	 * Get chat history by user ID
	 * 
	 * @param id of USER
	 * @throws JsonProcessingException
	 */
	@GetMapping(path = "/history/{idUser}", produces = "application/json")
	@Transactional
	@ResponseBody
	public String chatHistory(@PathVariable long idUser, Model model, HttpSession session) throws JsonProcessingException {
		List<Message> msgs = entityManager.createNamedQuery("Message.findAllByUserID", Message.class)
											.setParameter("idUser", idUser)
											.getResultList();
		
		if (msgs != null) {
			User user = entityManager.find(User.class, idUser);

			StringBuilder stringBuilder = new StringBuilder();

			stringBuilder.append("{ \"result\": \"accepted\", ");
			stringBuilder.append(String.format("\"clientName\": \"%s\", ", user.getFirstName() + ' ' + user.getLastName()));
			stringBuilder.append("\"messages\": [");

			for (Message m : msgs)
				stringBuilder.append(String.format("{\"text\": \"%s\", \"sender\": %b},", 
														m.getText(), m.getSender().getId() == idUser ? true : false));

			stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length()); // Remove last comma

			stringBuilder.append("]}");

			System.out.println(stringBuilder.toString());

			return stringBuilder.toString();
		}
		
		return "{\"result\": \"error\"}";
	}
}