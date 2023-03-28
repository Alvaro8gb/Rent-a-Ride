package es.ucm.fdi.iw.controller;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.Location;
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
    private LocalData localData;
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

        @GetMapping("{id}/pic")
    public StreamingResponseBody getPic(@PathVariable long id) throws IOException {
        File f = localData.getFile("vehicle", ""+id+".png");
        InputStream in = new BufferedInputStream(f.exists() ?
            new FileInputStream(f) : VehicleController.defaultPic());
        return os -> FileCopyUtils.copy(in, os);
    }

    private static InputStream defaultPic() {
	    return new BufferedInputStream(Objects.requireNonNull(
            VehicleController.class.getClassLoader().getResourceAsStream(
                "static/img/default-vehicle.png")));
    }
    
    @GetMapping(path = "/searchByName", produces = "application/json")
    @Transactional
	@ResponseBody
    public String searchByName(@RequestParam("filtro") String filtro, Model model) throws JsonProcessingException{
        List<Vehicle> l = entityManager.createNamedQuery("Vehicle.searchWithFilter", Vehicle.class).setParameter("filtro", filtro).getResultList();

        String jsonString = "{\"data\" : [";
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
   
    @PostMapping("/create")
    @Transactional
    public String create(Model model, @RequestParam(required=true) String marca,
                                      @RequestParam(required=true) String modelo,
                                      @RequestParam(required=true) int anio,
                                      @RequestParam(required=true) String combustible,
                                      @RequestParam(required=true) float consumo,
                                      @RequestParam(required=true) String cambio,
                                      @RequestParam(required=true) int puertas,
                                      @RequestParam(required=true) int plazas,
                                      @RequestParam(required=true) int cv,
                                      @RequestParam(required=true) String matricula,
                                      @RequestParam(required=true) int autonomia,
                                      @RequestParam(required=true) String recogida,
                                      @RequestParam(required=true) float precio) {
        
        Location location = entityManager.createNamedQuery("Location.byName", Location.class)
                                            .setParameter("name", recogida)
                                            .getSingleResult();
        
        Vehicle vehicle = new Vehicle();

        
        vehicle.setBrand(marca);
        vehicle.setModelName(modelo);
        vehicle.setOldYear(anio);
        vehicle.setFuel(Fuel.valueOf(combustible));
        vehicle.setConsumption(consumo);
        vehicle.setTransmission(Transmission.valueOf(cambio));
        vehicle.setDoors(puertas);
        vehicle.setSeats(plazas);
        vehicle.setCv(cv);
        vehicle.setLicense(matricula);
        vehicle.setAutonomy(autonomia);
        vehicle.setPriceByDay(precio);
        vehicle.setLocation(location);

        entityManager.persist(vehicle);
        entityManager.flush();

        return "createVehicle";
    }
}