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

        return "in_chats";
    }
    
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

	@GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    
    
}
