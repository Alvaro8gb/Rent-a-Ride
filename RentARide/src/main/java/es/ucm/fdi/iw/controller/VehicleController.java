package es.ucm.fdi.iw.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.ucm.fdi.iw.model.Vehicle;
import es.ucm.fdi.iw.model.Vehicle.Fuel;
import es.ucm.fdi.iw.model.Vehicle.Transmission;


/**
 *  Non-authenticated requests only.
 */
@Controller
@RequestMapping("vehicle")
public class VehicleController {

	private static final Logger log = LogManager.getLogger(VehicleController.class);

    @Autowired
	private EntityManager entityManager;

    @PostMapping("/search")
    public String search(Model model, 
                        @RequestParam(required=false) String vehicle,
                        @RequestParam(required=false) String pickupPoint,
                        @RequestParam(required=false) String startDate,
                        @RequestParam(required=false) String endDate) {
        /*
        log.info("vehicle {}", vehicle);
        log.info("pickupPoint {}", pickupPoint);
        log.info("startDate {}", startDate);
        log.info("endDate {}", endDate);
        */
        
        List<Vehicle> vs = entityManager.createNamedQuery("Vehicle.byVechicle", Vehicle.class)
        .setParameter("modelName", vehicle)
        .setParameter("location", pickupPoint)
        .getResultList();

        model.addAttribute("vehicles", vs);

        return "searchResult";
    }

    @GetMapping("{id}")
    public String index(Model model, @PathVariable long id){
        Vehicle target = entityManager.find(Vehicle.class, id);

        if ( target == null){
            model.addAttribute("status", 400);
            return "error";
        }

        model.addAttribute("vehicle", target);

        return "carDetails";
    }
    
    @GetMapping(path = "/searchByName", produces = "application/json")
    @Transactional
	@ResponseBody
    public String searchByName(@RequestParam("filtro") String filtro, Model model, @PathVariable long id) throws JsonProcessingException{
        List<Vehicle> l = entityManager.createQuery("Vehicle.searchWithFilter", Vehicle.class).setParameter("filtro", filtro).getResultList();

        String jsonString = "{'data' : [";
        ObjectMapper objectMapper = new ObjectMapper();
        
        int i = 0;
        for(Vehicle v : l){
            jsonString += objectMapper.writeValueAsString(v.toTransfer());
            if(i != l.size() - 1){
                jsonString += ", ";
            }
            i++;
        }

        jsonString += "]}";
        
        return jsonString;
    }
   
}