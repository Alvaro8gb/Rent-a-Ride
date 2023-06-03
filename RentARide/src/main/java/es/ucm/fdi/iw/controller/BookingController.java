package es.ucm.fdi.iw.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
import es.ucm.fdi.iw.model.Ticket;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Vehicle;

/**
 * Non-authenticated requests only.
 */
@Controller
@RequestMapping("booking")
public class BookingController {

    private static final Logger log = LogManager.getLogger(BookingController.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private EntityManager entityManager;

    @GetMapping("/{idBook}")
    public String history(Model model,  @PathVariable String idBook) {
        
        Booking b = entityManager.createNamedQuery("Booking.byId", Booking.class)
                                                .setParameter("id", idBook).getSingleResult();

        model.addAttribute("booking", b);

        log.info("Viendo el boking {} con id {}", b.toString(), idBook);

        return "book";
    }

    @PostMapping("/{idVehicle}")
    @Transactional
    public String booking(Model model, RedirectAttributes redirAttrs,
            @PathVariable long idVehicle,
            @RequestParam(required = true) String inDate,
            @RequestParam(required = true) String outDate,
            HttpSession session) {

        try {
            
            LocalDate inDateTime = LocalDate.parse(inDate, formatter);
            LocalDate outDateTime = LocalDate.parse(outDate, formatter);

            Vehicle vehicle = entityManager.find(Vehicle.class, idVehicle);
            User requester = (User) session.getAttribute("u");
            User user = entityManager.find(User.class, requester.getId());

            long daysDifference = ChronoUnit.DAYS.between(inDateTime, outDateTime) + 1;
        
            Booking target = new Booking(UserController.generateRandomBase64Token(8),
                    inDateTime, outDateTime,
                    daysDifference * vehicle.getPriceByDay(),
                    user, vehicle, false);

            entityManager.persist(target);
            entityManager.flush();

            redirAttrs.addFlashAttribute("successMessage", "La reserva se ha realizado con éxito");  
        } catch (Exception e) {
            redirAttrs.addFlashAttribute("errorMessage", "Ocurrió un problema realizando la reserva");
        }

        return String.format("redirect:/vehicle/%d", idVehicle);
    }

    @PostMapping("/delete/{id}")
    @Transactional
    public String delete(@PathVariable String id, RedirectAttributes redirAttrs, HttpSession session) {

        try { 

            Booking target = entityManager.find(Booking.class, id);
            

            if (!target.isCancelled()){

                target.setCancelled(true);
                entityManager.persist(target);
                entityManager.flush();
                redirAttrs.addFlashAttribute("successMessage", "Se ha solicitado la cancelación");

            }else{
                redirAttrs.addFlashAttribute("successMessage", "Ya se ha solicitado la cancelación");
            }           
        } catch (Exception e) {
            redirAttrs.addFlashAttribute("errorMessage", "Ocurrió un problema eliminando la resrva");
        }

        return "redirect:/booking/"+ id;
    }

    @PostMapping("confirmDelete/{id}")
    @Transactional
    public String confirm_delete(@PathVariable String id, RedirectAttributes redirAttrs, HttpSession session) {

        try { 
            Booking target = entityManager.find(Booking.class, id);
            entityManager.remove(target);
            entityManager.flush();
            redirAttrs.addFlashAttribute("successMessage", "La reserva se ha eliminado con éxito");
        } catch (Exception e) {
            redirAttrs.addFlashAttribute("errorMessage", "Ocurrió un problema eliminando la resrva");
        }

        // Comprobar si roll admin
        return "redirect:/booking/all";

    }

    @GetMapping(path = "/{idVehicle}", produces = "application/json")
    @ResponseBody
    public String viewBooking(Model model, @PathVariable long idVehicle) throws JsonProcessingException {

        Vehicle vehicle = entityManager.find(Vehicle.class, idVehicle);
        Booking book = vehicle.getBookings().size() > 0 ? vehicle.getBookings().get(0) : null;

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = book == null ? "{\"data\": false}"
                : "{\"data\": " + objectMapper.writeValueAsString(book.toTransfer()) + "}";

        log.info("booking {} {}", idVehicle, jsonString);

        return jsonString;
    }

    @GetMapping("list")
    public String list(Model model) {
        int threeWeeks = 21;
        SortedMap<LocalDate, List<Booking>> res = new TreeMap<>();
        LocalDate date = LocalDate.now();
        int dia = date.getDayOfWeek().getValue();

        switch (dia) { // Esto no seria mas faci hacer date = date.minusDays(dia+6)
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

        for (int i = 0; i < threeWeeks; i++) {
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

    @GetMapping("/all")
    @Transactional 
    public String findAllTickets(Model model) {

        List<Booking> books = entityManager.createNamedQuery("Booking.findAll", Booking.class).getResultList();

        model.addAttribute("books", books);

        return "booksManager";
    }

    @GetMapping("history")
    public String history(Model model, HttpSession session) {
        User requester = (User) session.getAttribute("u");
        List<Booking> bookings = entityManager.createNamedQuery("Booking.byUser", Booking.class)
                .setParameter("userID", requester.getId())
                .getResultList();

        model.addAttribute("bookings", bookings);
        model.addAttribute("gravitys", Arrays.asList(Ticket.Gravity.values()));

        log.info("El usuario {} tiene {} bookings", requester.getId(), bookings.size());

        return "bookingHistory";
    }

    @GetMapping(path = "/details", produces = "application/json")
    @ResponseBody
    public String getDetails(Model model, @RequestParam long vehicleID,
            @RequestParam long userID) throws JsonProcessingException {

        Vehicle vehicle = entityManager.find(Vehicle.class, vehicleID);
        User user = entityManager.find(User.class, userID);

        StringBuilder stringBuilder = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();

        stringBuilder.append("{");
        stringBuilder.append("\"user\": " + objectMapper.writeValueAsString(user.toTransfer()) + ", ");
        stringBuilder.append("\"vehicle\": " + objectMapper.writeValueAsString(vehicle.toTransfer()));
        stringBuilder.append("}");

        return stringBuilder.toString();
    }
}