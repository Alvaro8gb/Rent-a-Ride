package external;

import com.intuit.karate.junit5.Karate;

class ExternalRunner {
    
    @Karate.Test
    Karate testLogin() {
        return Karate.run("login").relativeTo(getClass());
    }    

    @Karate.Test
    Karate testWs() {
        return Karate.run("ws").relativeTo(getClass());
    } 
    
    @Karate.Test
    Karate testMain() {
        return Karate.run("main").relativeTo(getClass());
    }

    @Karate.Test
    Karate createVehicle() {
        return Karate.run("createVehicle").relativeTo(getClass());
    }
    @Karate.Test
    Karate createTicket() {
        return Karate.run("createTicket").relativeTo(getClass());
    }
}

