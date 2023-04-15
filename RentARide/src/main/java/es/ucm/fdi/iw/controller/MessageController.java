package es.ucm.fdi.iw.controller;

import java.util.List;
import java.util.stream.Collectors;
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

	@GetMapping("/in")
	public String inChats(Model model, HttpSession session) {

		long userId = ((User) session.getAttribute("u")).getId();
		User u = entityManager.find(User.class, userId);

		model.addAttribute("msgs", u.getReceived());
		model.addAttribute("msgsPending", pendingMsgs.size());

		Message lastMsg = pendingMsgs.size() > 0 ? pendingMsgs.iterator().next() : null;
		model.addAttribute("lastMsg", lastMsg);

		return "messages";
	}

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	private Queue<Message> pendingMsgs;

	public MessageController() {
		pendingMsgs = new LinkedList<Message>();

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

		String text = o.get("message").asText();
		Message m = new Message();
		User sender = entityManager.find(User.class, ((User) session.getAttribute("u")).getId());
		m.setSender(sender);
		m.setDateSent(LocalDateTime.now());
		m.setText(text);

		if (idUser == 0) {

			pendingMsgs.add(m); // Guardamos mensaje en la cola

		} else {

			User u = entityManager.find(User.class, idUser);

			m.setRecipient(u);
			entityManager.persist(m);
			entityManager.flush(); // to get Id before commit

			sendMsg(m, u); // Enviamos
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
	@PostMapping(path = "/{idMessage}")
	@ResponseBody
	@Transactional
	public String acceptMsg(@PathVariable long idMessage,
			@RequestBody JsonNode o, Model model, HttpSession session)
			throws JsonProcessingException {

		Message m = pendingMsgs.iterator().next();

		if (m.getId() == idMessage){

			User recept = entityManager.find(User.class, ((User) session.getAttribute("u")).getId());

			m.setRecipient(recept);

			entityManager.persist(m);
			entityManager.flush(); 

			sendMsg(m, recept); 

			pendingMsgs.remove();

			return "{\"result\": \"message accept.\"}";

		}

		return "{\"result\": \"false\"}";
		
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
}