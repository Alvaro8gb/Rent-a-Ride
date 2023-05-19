package es.ucm.fdi.iw.controller;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.ucm.fdi.iw.model.User;

/**
 *  Site administration.
 *
 *  Access to this end-point is authenticated - see SecurityConfig
 */
@Controller
@RequestMapping("admin")
public class AdminController {

	private static final Logger log = LogManager.getLogger(AdminController.class);


    @Autowired
    private EntityManager entityManager;
    
	@GetMapping("/")
    public String index(Model model) {
        return "admin";
    }

    
    @GetMapping("/search")
    public String search(Model model){
        return "";
    }

    @GetMapping("/userList")
    public String userList(Model model) {
        List<User> users = entityManager.createNamedQuery("User.all", User.class).getResultList();
        model.addAttribute("users", users);
        return "userList";
    }
}
