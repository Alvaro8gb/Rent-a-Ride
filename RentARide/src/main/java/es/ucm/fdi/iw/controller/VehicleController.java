package es.ucm.fdi.iw.controller;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.Vehicle;


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

        log.info(" vehicle {}", vehicle);
        log.info("pickupPoint {}", pickupPoint);
        log.info("startDate {}", startDate);
        log.info("endDate {}", endDate);

        List<Vehicle> vs = entityManager.createNamedQuery("Vehicle.byVechicle", Vehicle.class).
        setParameter("vehicle", vehicle).getResultList();

        log.info(" Entity {}", vs);

        return "index";
    }

}