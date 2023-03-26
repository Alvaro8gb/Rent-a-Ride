package es.ucm.fdi.iw.DummyClasses;

import es.ucm.fdi.iw.model.Vehicle;
import es.ucm.fdi.iw.model.Vehicle.Fuel;
import es.ucm.fdi.iw.model.Vehicle.Transmission;

import java.util.Random;

public class VehicleDummy {

    private static final Random random = new Random();

    public static Vehicle generateDummyVehicle() {
        Vehicle vehicle = new Vehicle();
        /*
        vehicle.setVehicle("Ferrari");
        vehicle.setDescription(" Esto sera la info de un coche");
        vehicle.setOldYear(numberIntBetween(2000, 2022));
        vehicle.setFuel(Fuel.values()[random.nextInt(Fuel.values().length)]);
        vehicle.setCityConsumption(numberFloatBetween(2, 5));
        vehicle.setRoadConsumption(numberFloatBetween(2, 10));
        vehicle.setTransmission(Transmission.values()[random.nextInt(Transmission.values().length)]);
        vehicle.setDoors(numberIntBetween(2, 5));
        vehicle.setSeats(numberIntBetween(2, 9));
        vehicle.setAutonomy(numberIntBetween(300, 800));
        */
        return vehicle;
        
    }

    private static int numberIntBetween(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    private static float numberFloatBetween(int min, int max) {
        return 0;
    }
}
