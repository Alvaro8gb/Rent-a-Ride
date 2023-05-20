package es.ucm.fdi.iw.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.ucm.fdi.iw.model.Ticket;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Vehicle;

@Controller
@RequestMapping("incidencias")
public class TicketsController {

    private static final Logger log = LogManager.getLogger(MessageController.class);

    @Autowired
    private EntityManager entityManager;

    @GetMapping("/")
    public String userTickets(Model model, HttpSession session) {
        User user = entityManager.find(User.class, ((User) session.getAttribute("u")).getId());

        List<Ticket> pendingMsgs = user.getTickets();

        model.addAttribute("tickets", pendingMsgs); // Return messages queue

        return "tickets";
    }

    @PostMapping("/{id}/delete")
    @Transactional
    public String delete(@PathVariable long id, RedirectAttributes redirAttrs, HttpSession session) {

        try {

            Ticket target = entityManager.find(Ticket.class, id);
            entityManager.remove(target);
            entityManager.flush();
            redirAttrs.addFlashAttribute("successMessage", "El ticket se ha eliminado con éxito");
        } catch (Exception e) {
            redirAttrs.addFlashAttribute("errorMessage", "Ocurrió un problema eliminando el ticket");
        }

        // Comprobar si roll admin
        return "redirect:/incidencias/";

    }

    @GetMapping(value = "/export", produces = "text/csv")
    public ResponseEntity<String> exportCSV() {
        List<Ticket> ts = entityManager.createNamedQuery("Ticket.findAll", Ticket.class).getResultList();

        String csvData = Ticket.serializeToCSV(ts);

        // Devuelve una respuesta con el contenido CSV en el cuerpo
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header("Content-Disposition", "attachment; filename=\"tickets.csv\"")
                .body(csvData);
    }

    @PostMapping("ticket/{idVehicle}")
    @Transactional
    public String ticket(Model model, RedirectAttributes redirAttrs,
            @PathVariable long idVehicle,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String gravity,
            HttpSession session) {

        try {
            Vehicle vehicle = entityManager.find(Vehicle.class, idVehicle);
            User requester = (User) session.getAttribute("u");
            Ticket ticket = new Ticket();

            ticket.setGravity(Ticket.Gravity.valueOf(gravity));
            ticket.setIdVehicle(vehicle.getId());
            ticket.setIdUser(requester.getId());
            ticket.setOcurranceDate(LocalDate.now());
            ticket.setText(text);

            entityManager.persist(ticket);
            entityManager.flush();

            redirAttrs.addFlashAttribute("successMessage", "El ticket se registro con exito");
        } catch (Exception e) {
            redirAttrs.addFlashAttribute("errorMessage", "Ocurrió un problema creando el ticket");
        }

        return "redirect:/booking/history";
    }

    @GetMapping("/list")
    public String findAllTickets(Model model) {

        List<Ticket> tickets = entityManager.createNamedQuery("Ticket.findAll", Ticket.class).getResultList();

        model.addAttribute("tickets", tickets);

        return "ticketsManager";
    }

}
