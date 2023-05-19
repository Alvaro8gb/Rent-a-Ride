package es.ucm.fdi.iw.controller;

import es.ucm.fdi.iw.model.Booking;
import es.ucm.fdi.iw.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * User management.
 *
 * Access to this end-point is authenticated.
 */
@Controller()
@RequestMapping("gestor")
public class ManagerController {

    @Autowired
    private EntityManager entityManager;

    @GetMapping("/incidencias")
    public String showTickets(Model model) {

        List<Ticket> tickets = entityManager.createNamedQuery("Ticket.findAll", Ticket.class).getResultList();

        model.addAttribute("tickets", tickets);

        return "ticketsManager";
    }

    @GetMapping("calendario")
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



}
