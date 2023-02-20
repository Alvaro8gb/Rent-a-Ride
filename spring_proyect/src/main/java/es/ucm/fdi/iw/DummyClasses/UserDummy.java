package es.ucm.fdi.iw.DummyClasses;

import java.util.Random;
import java.util.UUID;

public class UserDummy {

    private static final String[] NAMES = { "Juan", "María", "Pedro", "Ana", "Luis", "Laura", "David", "Lucía",
            "Jorge", "Elena" };
    private static final String[] LAST_NAMES = { "García", "López", "Martínez", "Sánchez", "Pérez", "Gómez", "Fernández",
            "Rodríguez", "Hernández", "Ruiz" };
    private static final String[] MAIL_DOMAINS = { "gmail.com", "hotmail.com", "outlook.com", "yahoo.com",
            "aol.com" };
    private static final Random random = new Random();

    private String name;

    private String last_name;
    private String mail;
    private String pass;

    public UserDummy(String name, String last_name, String mail, String pass) {
        this.pass = pass;
        this.last_name = last_name;
        this.mail = name;
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getLastName() {
        return last_name;
    }
    public String getPass(){
        return pass;
    }

    public static UserDummy generateUser() {
        String name = NAMES[random.nextInt(NAMES.length)];
        String last_name = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        String mail = name.toLowerCase() + "." + last_name.toLowerCase() + "@" + MAIL_DOMAINS[random.nextInt(MAIL_DOMAINS.length)];
        String pass = UUID.randomUUID().toString().substring(0, 8);
        return new UserDummy(name, last_name, mail, pass);
    }
}
