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
    Karate testCreateVehicle() {
        return Karate.run("createVehicle").relativeTo(getClass());
    }
    @Karate.Test
    Karate testCreateTicket() {
        return Karate.run("createTicket").relativeTo(getClass());
    }

    @Karate.Test
    Karate testSecurity() {
        return Karate.run("security").relativeTo(getClass());
    }

    @Karate.Test
    Karate testModUsuario() {
        return Karate.run("modUser").relativeTo(getClass());
    }

}

