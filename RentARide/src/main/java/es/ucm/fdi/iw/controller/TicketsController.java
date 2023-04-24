package es.ucm.fdi.iw.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.ucm.fdi.iw.model.Ticket;
import es.ucm.fdi.iw.model.User;

@Controller
@RequestMapping("incidencias")
public class TicketsController {

    private static final Logger log = LogManager.getLogger(MessageController.class);

	@Autowired
	private EntityManager entityManager;
    
    @GetMapping("/")
	public String inChats(Model model, HttpSession session) {
        User user = entityManager.find(User.class, ((User) session.getAttribute("u")).getId());

        List<Ticket> pendingMsgs = user.getTickets();

		model.addAttribute("tickets", pendingMsgs); // Return messages queue

		return "tickets";
	}


	@PostMapping("/{id}/delete")
	@Transactional
	public String delete(@PathVariable long id, RedirectAttributes redirAttrs, HttpSession session) {
		Ticket target = entityManager.find(Ticket.class, id);
		entityManager.remove(target);
		entityManager.flush();
		redirAttrs.addFlashAttribute("successMessage", "El ticket se ha eliminado con éxito");

		// Comprobar si roll admin
		return "redirect:/incidencias/";
		
	}
}
