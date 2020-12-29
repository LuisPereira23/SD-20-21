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

    public CovidAlarm(){
        this.users = new HashMap<>();
        lock = new ReentrantLock();
    }

    public void setUser(HashMap<String,User> u){
        this.users = u;
    }
    public Map<String,User> getUser(){
        return this.users;
    }

    public String convertWithStream() {
        Map<String, User> map = getUser();
        String mapAsString = map.keySet().stream()
                .map(key -> key + "=" + map.get(key).getPassword())
                .collect(Collectors.joining(", ", "{", "}"));
        return mapAsString;
    }

    public Boolean OptionChose(DataInputStream in) throws IOException{
        Packet packet = Packet.deserialize(in);

        switch (packet.getOption()) {
            case 1 -> {
                lock.lock();
                try {
                    return AuthUser(packet);
                }finally {
                    lock.unlock();
                }
            }
            case 2 -> {
                lock.lock();
                try {
                    return userRegister(packet);
                }finally {
                    lock.unlock();
                }
            }
            default -> {
                return false;
            }
        }
    }

    public boolean userRegister(Packet packet){
        boolean y = false;
        String username = packet.getUsername();
        String pass = packet.getPassword();
        if(!this.users.containsKey(username)){
            User u = new User(username,pass);
            this.users.put(username, u);
            y = true;
        }
        return y;
    }

    public boolean AuthUser(Packet packet){
        boolean y = false;
        String username = packet.getUsername();
        String pass = packet.getPassword();
            if(this.users.containsKey(username)) {
                this.users.get(username).getPassword().equals(pass);
                y = true;
            }
        return y;
    }
}
