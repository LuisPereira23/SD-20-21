import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CovidAlarm implements Serializable {
    private Map<String, User> users;

    public CovidAlarm(){
        this.users = new HashMap<>();
    }

    public void setUser(HashMap<String,User> u){
        this.users = u;
    }
    public Map<String,User> getUser(){
        return this.users;
    }

    public boolean userRegister(String username, String pass){
        boolean y = false;
        if(!this.users.containsKey(username)){
            User u = new User(username,pass);
            this.users.put(username, u);
            y = true;
        }
        return y;
    }

    public boolean AuthUser(String username, String pass){
        boolean y = false;
            if(this.users.containsKey(username)) {
                this.users.get(username).getPassword().equals(pass);
                y = true;
            }
        return y;
    }
}
