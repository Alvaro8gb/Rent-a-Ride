package es.ucm.fdi.iw.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Vehicle;


/**
 *  Non-authenticated requests only.
 */
@Controller
public class RootController {

	private static final Logger log = LogManager.getLogger(RootController.class);

    @Autowired
	private EntityManager entityManager;

    public RootController() {
        //generateDummy();
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
    
    @GetMapping("/createVehicle")
    public String createVehicle(Model model) {
        return "createVehicle";
    }
  
	@GetMapping("/")
    public String index(Model model) {
        List<Vehicle> vs = entityManager.createNamedQuery("Vehicle.findAll", Vehicle.class).getResultList();
        List<String> locations = entityManager.createNamedQuery("Vehicle.allLocation", String.class).getResultList();
        model.addAttribute("vehicles", vs);
        model.addAttribute("locations", locations);

        //TO-DO: Logica de seleccion de recepto de mensajes que sera un gestor, ver gestores activos y seleccionar uno
        //como hay creado siempre un gestor se le asigna a el todos los mensajes  
        model.addAttribute("idReceiver", 2);
        return "index";
    }

    @GetMapping("/carsManagement")
    public String carsManagment(Model model,
                                @RequestParam(required = false) boolean available){
        
        List<Vehicle> vs = entityManager.createNamedQuery("Vehicle.findAll", Vehicle.class).getResultList();
        model.addAttribute("vehicles", vs);
        return "carsManagement";
    }

    @GetMapping("/userList")
    public String userList(Model model) {
        List<User> users = entityManager.createNamedQuery("User.all", User.class).getResultList();
        model.addAttribute("users", users);
        return "userList";
    }

    @GetMapping("/inChats")
    public String inChats( Model model){
	    List<Message> msgs = entityManager.createNamedQuery("Message.allClients", Message.class).getResultList();

        model.addAttribute("msgs", msgs);
        
        return "inChats";
    }

    @GetMapping("/carDetails")
    public String carDetails(Model model) {
        return "carDetails";
    }

    @GetMapping("/carsCalendar")
    public String carCalendar(Model model) {
        return "carsCalendar";
    }

}
