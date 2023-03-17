package es.ucm.fdi.iw.DummyClasses;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class ChatDummy {
    private String name;
    private String msg;

    private static final Random random = new Random();
    private static final String FILENAME = "static/dummy/dummy_messages.txt";
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
        try ( BufferedReader br = new BufferedReader(
            new InputStreamReader(
                Objects.requireNonNull(
                    ChatDummy.class.getClassLoader().getResourceAsStream(FILENAME)), "utf-8"))){
            
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
