package es.ucm.fdi.iw.controller;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import es.ucm.fdi.iw.DummyClasses.ChatDummy;
import es.ucm.fdi.iw.DummyClasses.UserDummy;

/**
 *  Non-authenticated requests only.
 */
@Controller
public class RootController {

	private static final Logger log = LogManager.getLogger(RootController.class);
    private static ArrayList<UserDummy> usersDummy;
    private static ArrayList<ChatDummy> chatsDummy;

    public RootController(){

        generateDummy();
    }

    private static void generateDummy(){
        usersDummy = new ArrayList<>();
        chatsDummy = new ArrayList<>();
       
        ChatDummy.leerMensajes();

        for( int i = 0; i < 10; i++){
            chatsDummy.add(ChatDummy.generateChat());
        }

        for(int i = 0; i < 10; i++){
            usersDummy.add(UserDummy.generateUser());
        }
    }
    @GetMapping("/profile")
    public String profile(Model model) {
        return "profile";
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
        
        model.addAttribute("users", usersDummy);
        return "userList";
    }

    @GetMapping("/inChats")
    public String inChats(Model model) {

        model.addAttribute("chats", chatsDummy);
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