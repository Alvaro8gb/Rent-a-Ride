package es.ucm.fdi.iw.controller;

import javax.servlet.http.HttpSession;
import javax.persistence.EntityManager;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.Arrays;
import java.util.List;

import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Valoracion;

@Controller
@RequestMapping("valoracion")
public class ValoracionController {

    @Autowired
	private EntityManager entityManager;


    @GetMapping("/valoraciones")
    public String history(Model model) {
        List<Valoracion> valoraciones = entityManager.createNamedQuery("Valoracion.getAll",Valoracion.class).getResultList();

        model.addAttribute("valoraciones", valoraciones);
        

        

        return "valoracionesList";
    }
}
