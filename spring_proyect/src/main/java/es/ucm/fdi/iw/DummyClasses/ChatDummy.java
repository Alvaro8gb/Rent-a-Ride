package es.ucm.fdi.iw.DummyClasses;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ChatDummy {
    private String name;
    private String msg;

    private static final Random random = new Random();
    private static final String FILENAME = "spring_proyect/src/main/java/es/ucm/fdi/iw/DummyClasses/dummy_messages.txt";
    private static ArrayList<String> mensajes = new ArrayList<String>();

    private static final String[] NAMES = { "Juan", "María", "Pedro", "Ana", "Luis", "Laura", "David", "Lucía",
            "Jorge", "Elena" };
    
    public ChatDummy(String name, String msg) {
        this.name = name;
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }

    public static void leerMensajes() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                mensajes.add(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ChatDummy generateChat() {
        String name = NAMES[random.nextInt(NAMES.length)].toLowerCase();
        name += "." + String.format("%04d", random.nextInt(10000));
        String msg =  mensajes.get(random.nextInt(mensajes.size()));

        return new ChatDummy(name, msg);
    }

    public static void main(String[] args) {
        ChatDummy.leerMensajes();
        System.out.println(ChatDummy.generateChat().getMsg());
    }
}
