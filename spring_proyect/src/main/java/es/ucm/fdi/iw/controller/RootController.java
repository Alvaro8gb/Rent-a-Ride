package es.ucm.fdi.iw.controller;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import es.ucm.fdi.iw.DummyClasses.ChatDummy;

/**
 *  Non-authenticated requests only.
 */
@Controller
public class RootController {

	private static final Logger log = LogManager.getLogger(RootController.class);

    @GetMapping("/profile")
    public String profile(Model model) {
        return "profile";
    }

    @GetMapping("/inChats")
    public String in_chats(Model model) {

        ArrayList<ChatDummy> chats = new ArrayList<>();
       
        ChatDummy.leerMensajes();

        for( int i = 0; i < 10; i++){
            chats.add(ChatDummy.generateChat());
        }

        model.addAttribute("chats", chats);

        return "inChats";
    }
    
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
    
    @GetMapping("/createVehicle")
    public String createVehicle(Model model) {
        return "createVehicle";
    }
    @GetMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }
	@GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/carsManagement")
    public String carsManagment(Model model) {
        return "carsManagement";
    }

    @GetMapping("/userList")
    public String userList(Model model) {
        return "userList";
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
