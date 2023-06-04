package es.ucm.fdi.iw.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import es.ucm.fdi.iw.model.Vehicle;

@Controller
public class RootController {

    private static final Logger log = LogManager.getLogger(RootController.class);

    @Autowired
    private EntityManager entityManager;

    public RootController() {

    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Vehicle> vs = entityManager.createNamedQuery("Vehicle.findAll", Vehicle.class).getResultList();
        List<String> locations = entityManager.createNamedQuery("Vehicle.allLocation", String.class).getResultList();

        model.addAttribute("vehicles", vs);
        model.addAttribute("locations", locations);

        // Se envia mensaje a la cola
        model.addAttribute("idReceiver", 0);

        return "index";
    }
}
