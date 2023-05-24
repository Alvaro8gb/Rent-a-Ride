package es.ucm.fdi.iw.controller;

import javax.servlet.http.HttpSession;
import javax.persistence.EntityManager;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Valoracion;
import es.ucm.fdi.iw.model.ValoracionID;
import es.ucm.fdi.iw.model.Vehicle;
import es.ucm.fdi.iw.model.User.Role;
import lombok.val;

@Controller
@RequestMapping("valoracion")
public class ValoracionController {

    @Autowired
	private EntityManager entityManager;
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 

    @GetMapping("/valoraciones")
    public String history(Model model) {
        List<Valoracion> valoraciones = entityManager.createNamedQuery("Valoracion.getAll",Valoracion.class).getResultList();
        model.addAttribute("valoraciones", valoraciones);
        return "valoracionesList";
    }

    @PostMapping("/nuevaValoracion/{idVehicle}")
    @Transactional
    public String search(@PathVariable long idVehicle,
                        @RequestParam(required=false) String texto,
                        @RequestParam(required=false) int rating,
                         RedirectAttributes redir,
                         HttpSession session) {
                            
        User requester = (User)session.getAttribute("u");
        User user = entityManager.find(User.class, requester.getId());
        if(requester.hasRole(Role.USER) || requester.hasRole(Role.GESTOR)){
            ValoracionID valoracionID = new ValoracionID(idVehicle, requester.getId());
            LocalDate fecha = LocalDate.now();
            fecha.format(formatter);
            Vehicle vehicle = entityManager.find(Vehicle.class, idVehicle);
            Valoracion target = new Valoracion(valoracionID, rating, texto, fecha, user, vehicle);
            entityManager.persist(target);
            entityManager.flush();
            redir.addFlashAttribute("successMessage", "La valoracion se ha realizado con éxito");
        }
        else{
            redir.addFlashAttribute("errorMessage", "Ocurrió un problema realizando la valoracion");
        }
        return "redirect:/booking/history";
    }
}
