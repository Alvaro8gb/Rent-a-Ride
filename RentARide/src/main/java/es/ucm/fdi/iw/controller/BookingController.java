package es.ucm.fdi.iw.controller;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

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
import es.ucm.fdi.iw.model.Ticket;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Vehicle;


/**
 *  Non-authenticated requests only.
 */
@Controller
@RequestMapping("booking")
public class BookingController {

	private static final Logger log = LogManager.getLogger(BookingController.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 


    @Autowired
	private EntityManager entityManager;

    @PostMapping("book/{idVehicle}")
    @Transactional
    public String booking(Model model, RedirectAttributes redirAttrs,
                            @PathVariable long idVehicle,
                            @RequestParam(required=false) String inDate,
                            @RequestParam(required=false) String outDate,
                            @RequestParam(required=false) Float precio,
                            HttpSession session){
                                
        try {
            Vehicle vehicle = entityManager.find(Vehicle.class, idVehicle);
            User requester = (User)session.getAttribute("u");
            LocalDate inDateTime = LocalDate.parse(inDate, formatter);
            LocalDate outDateTime = LocalDate.parse(outDate, formatter);
            BookingID bookingID = new BookingID(idVehicle, requester.getId(), inDateTime, outDateTime);
            User user = entityManager.find(User.class, requester.getId());
            Booking target = new Booking(bookingID, precio, user, vehicle);
            
            entityManager.persist(target);
            entityManager.flush();

            redirAttrs.addFlashAttribute("successMessage", "La reserva se ha realizado con éxito");
        } catch (Exception e) {
            redirAttrs.addFlashAttribute("errorMessage", "Ocurrió un problema realizando la reserva");
        }

        
        return String.format("redirect:/vehicle/%d", idVehicle);
    }

    @GetMapping(path="/{idVehicle}", produces = "application/json")
    @ResponseBody
    public String viewBooking(Model model, @PathVariable long idVehicle) throws JsonProcessingException{
  
        Vehicle vehicle = entityManager.find(Vehicle.class, idVehicle);
        Booking book = vehicle.getBookings().size() > 0? vehicle.getBookings().get(0): null;
        
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = book == null? "{\"data\": false}" : "{\"data\": " +objectMapper.writeValueAsString(book.toTransfer()) +"}";

        log.info("booking {} {}", idVehicle, jsonString);

        return jsonString;
    }

    @GetMapping("list")
    public String list(Model model) {
        int threeWeeks = 21;
        SortedMap<LocalDate, List<Booking>> res = new TreeMap<>();
        LocalDate date = LocalDate.now();
        int dia = date.getDayOfWeek().getValue();

        switch(dia) {
            case 1:
                date = date.minusDays(7);
                break;
            case 2:
                date = date.minusDays(8);
                break;
            case 3:
                date = date.minusDays(9);
                break;
            case 4:
                date = date.minusDays(10);
                break;
            case 5:
                date = date.minusDays(11);
                break;
            case 6:
                date = date.minusDays(12);
                break;
            case 7:
                date = date.minusDays(13);
                break;
        }

        for(int i = 0; i < threeWeeks; i++) {
            List<Booking> data = entityManager.createNamedQuery("Booking.bydate", Booking.class)
            .setParameter("in_date", date)
            .setParameter("out_date", date)
            .getResultList();

            if (data.size() != 0)
                res.put(date, data);
            else
                res.put(date, null);

            date = date.plusDays(1);
        }

        model.addAttribute("bookings", res);
        
        return "listBookings";
    }

    @GetMapping("history")
    public String history(Model model, HttpSession session) {
        User requester = (User)session.getAttribute("u");
        List<Booking> bookings = entityManager.createNamedQuery("Booking.byUser", Booking.class)
                                                .setParameter("userID", requester.getId())
                                                .getResultList();

        model.addAttribute("bookings", bookings);
        model.addAttribute("gravitys", Arrays.asList(Ticket.Gravity.values()));

        log.info("El usuario {} tiene {} bookings", requester.getId(), bookings.size());

        return "bookingHistory";
    }

    @PostMapping("ticket/{idVehicle}")
	@Transactional
    public String ticket(Model model, RedirectAttributes redirAttrs,
                        @PathVariable long idVehicle,
                        @RequestParam(required=false) String text,
                        @RequestParam(required=false) String gravity,
                        HttpSession session){

        try {
            Vehicle vehicle = entityManager.find(Vehicle.class, idVehicle);
            User requester = (User)session.getAttribute("u");
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
}