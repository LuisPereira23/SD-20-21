import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class CovidAlarm implements Serializable {
    private Map<String, User> users;
    private ReentrantLock lock;
    private String info;

    public CovidAlarm(){
        this.users = new HashMap<>();
        lock = new ReentrantLock();
        this.info = null;
    }

    public void setUser(HashMap<String,User> u){ this.users = u; }
    public Map<String,User> getUser(){
        return this.users;
    }
    public String getInfo() { return this.info; }

    public String convertWithStream() {
        Map<String, User> map = getUser();
        return map.keySet().stream()
                .map(key -> key + "=" + map.get(key).getPassword())
                .collect(Collectors.joining(", ", "{", "}"));
    }

    public void optionResult(DataInputStream in) throws IOException{
        Packet packet = Packet.deserialize(in);

        switch (packet.getOption()) {
            case 1 -> {
                lock.lock();
                try {
                    authUser(packet);
                }finally {
                    lock.unlock();
                }
            }
            case 2 -> {
                lock.lock();
                try {
                    userRegister(packet);
                }finally {
                    lock.unlock();
                }
            }
            case 3 -> {
                lock.lock();
                try {
                    reportCovid(packet);
                }finally {
                    lock.unlock();
                }
            }
            default -> { }
        }
    }

    public void authUser(Packet packet){
        String username = packet.getUsername();
        String pass = packet.getPassword();
        if (this.users.containsKey(username)){
            if (this.users.get(username).getPassword().equals(pass))
                this.info = "Autenticação válida.";
        }
        else
            this.info = "Erro de autenticação.";
    }

    public void userRegister(Packet packet){
        String username = packet.getUsername();
        String pass = packet.getPassword();
        if(!this.users.containsKey(username)){
            User u = new User(username,pass);
            this.users.put(username, u);
            this.info = "Registo efetuado com sucesso.";
        }
        else
            this.info = "Nome de utilizador já está em uso.";
    }

    public void reportCovid(Packet packet){
        String username = packet.getUsername();
        this.users.get(username).setState(true);

        /*
        notificar localizações, etc ...
        */

        this.users.remove(username);
        this.info = "Caso de infeção registado com sucesso.";
    }

}
