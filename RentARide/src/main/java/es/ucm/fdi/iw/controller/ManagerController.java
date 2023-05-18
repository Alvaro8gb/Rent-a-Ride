package es.ucm.fdi.iw.controller;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.Ticket;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.User.Role;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;



import java.io.*;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

}
