package es.ucm.fdi.iw.controller;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.model.Booking;
import es.ucm.fdi.iw.model.BookingID;
import es.ucm.fdi.iw.model.Transferable;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Vehicle;
import es.ucm.fdi.iw.model.Booking.Transfer;


/**
 *  Non-authenticated requests only.
 */
@Controller
@RequestMapping("booking")
public class BookingController {

	private static final Logger log = LogManager.getLogger(BookingController.class);

    @Autowired
	private EntityManager entityManager;

    @PostMapping("book/{id}")
    @Transactional
    public String booking(Model model, RedirectAttributes redirAttrs,
                            @PathVariable long id,
                            @RequestParam(required=false) String inDate,
                            @RequestParam(required=false) String outDate,
                            @RequestParam(required=false) Float precio,
                            HttpSession session){
                                
        try {
            Vehicle vehicle = entityManager.find(Vehicle.class, id);
            User requester = (User)session.getAttribute("u");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
            LocalDate inDateTime = LocalDate.parse(inDate, formatter);
            LocalDate outDateTime = LocalDate.parse(outDate, formatter);
            BookingID bookingID = new BookingID(id, requester.getId(), inDateTime, outDateTime);
            User user = entityManager.find(User.class, requester.getId());
            Booking target = new Booking(bookingID, precio, user, vehicle);
            
            entityManager.persist(target);
            entityManager.flush();

            redirAttrs.addFlashAttribute("successMessage", "La reserva se ha realizado con éxito");
        } catch (Exception e) {
            redirAttrs.addFlashAttribute("errorMessage", "Ocurrió un problema realizando la reserva");
        }

        
        return String.format("redirect:/vehicle/%d", id);
    }

    @GetMapping(path="/{idVehicle}", produces = "application/json")
    @ResponseBody
    public String viewBooking(Model model, @PathVariable long idVehicle) throws JsonProcessingException{ // Transferable<Booking.Transfer>
        Booking book = new Booking(new BookingID(1, 2, null, null), (float)43, null, null);
        // cambiar por consulta a BD
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(book.toTransfer());
        
        log.info("booking {} {}", idVehicle, jsonString);

        return jsonString;
    }

}