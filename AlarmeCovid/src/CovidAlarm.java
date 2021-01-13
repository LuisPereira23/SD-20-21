import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class CovidAlarm implements Serializable {

    @Serial
    private static final long serialVersionUID = 3726281774063155278L;
    private Map<String, User> users;
    UserMap usermap;
    private ReentrantLock lock;
    private String info;

    public CovidAlarm(UserMap map){
        this.usermap = map;
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
            case 4 -> {
                lock.lock();
                try {
                    changePosition(packet);
                }finally {
                    lock.unlock();
                }
            }
            case 5 -> {
                lock.lock();
                try {
                    checkPosition(packet);
                }finally {
                    lock.unlock();
                }
            }
            case 6 -> {
                lock.lock();
                try {
                    checkNumber(packet);
                }finally {
                    lock.unlock();
                }
            }
            case 7 -> {
                lock.lock();
                try {
                    checkState(packet);
                }finally {
                    lock.unlock();
                }
            }
            case 8 -> checkMap();
            case 9 -> {
                lock.lock();
                try {
                    checkSpecial(packet);
                } finally {
                    lock.unlock();
                }
            }
            default -> { }
        }
    }

    public void authUser(Packet packet){
        String username = packet.getUsername();
        String pass = packet.getPassword();
        if (this.users.containsKey(username) && this.users.get(username).getState().equals(false)){
            if (this.users.get(username).getPassword().equals(pass))
                this.info = "Autenticação válida.";
        }
        else
            this.info = "Erro de autenticação.";
    }

    public void userRegister(Packet packet){
        String username = packet.getUsername();
        String pass = packet.getPassword();
        Boolean special = packet.getSpecial();
        int m = packet.getM();
        int n = packet.getN();
        Position p = new Position(m,n);
        if(!this.users.containsKey(username)){
            User user = new User(username,pass,special,p);
            usermap.addUser(p,user);
            this.users.put(username, user);
            this.info = "Registo efetuado com sucesso.";

            for (User other : this.users.values()) {
                if (!other.getUsername().equals(username) && other.getCurrent().equals(p)) {
                    other.addNearby(user);
                    user.addNearby(other);
                }
            }
        }
        else
            this.info = "Nome de utilizador já está em uso.";
    }

    public void reportCovid(Packet packet){
        String username = packet.getUsername();
        User user = this.users.get(username);
        this.users.get(username).setState(true);
        int m = packet.getM();
        int n = packet.getN();
        Position p = new Position(m,n);
        user.setCurrent(p);
        this.info = "Caso de infeção registado com sucesso.";
    }

    public void changePosition(Packet packet){
        String username = packet.getUsername();
        User user = this.users.get(username);
        int m = packet.getM();
        int n = packet.getN();
        Position p = new Position(m,n);
        user.setCurrent(p);

        for (User other : this.users.values()) {
            if (!other.getUsername().equals(username) && other.getCurrent().equals(p)) {
                other.addNearby(user);
                user.addNearby(other);
            }
        }


        this.usermap.addUser(p.clone(),user);
    }

    public void checkPosition(Packet packet){
        int m = packet.getM();
        int n = packet.getN();
        Position p = new Position(m,n);
        boolean result = false;
        for (User user : this.users.values()) {
            if (user.getCurrent().equals(p)) {
                result=true;
            }
        }

        this.info = Boolean.toString(result);
    }

    public void checkNumber(Packet packet){
        int m = packet.getM();
        int n = packet.getN();
        Position p = new Position(m,n);
        int num = 0;

        for (User user : this.users.values()) {
            if (user.getCurrent().equals(p)) {
                num++;
            }
        }
        this.info = Integer.toString(num);
    }

    public void checkState(Packet packet){
        String username = packet.getUsername();
        User user = this.users.get(username);
        boolean result = false;
        List<User> list = user.getNearbyUsers();
        for (User other : list) {
            if (other.getState()) {
                result = true;
            }
        }
        this.info = Boolean.toString(result);
    }

    public void checkSpecial(Packet packet){
        String username = packet.getUsername();
        User user = this.users.get(username);
        this.info=user.getSpecial().toString();
    }

    public void checkMap(){
        this.info=usermap.stringMap();
    }


    public void saveCovid(){
        lock.lock();
        try {
            IObjectStream a = new ObjectStream();
            a.saveServer(this);
        }finally {
            lock.unlock();
        }
    }
}
