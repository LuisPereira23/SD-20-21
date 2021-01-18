import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = -7149009667160496245L;
    private String username;
    private String password;
    private Boolean state; // Infected: True or False
    private Boolean special;
    private List<User> nearbyUsers;
    private Position current;


    public User(String username, String password,boolean special, Position current) {
        this.username = username;
        this.password = password;
        this.state = false;
        this.special = special;
        this.nearbyUsers = new ArrayList<>();
        this.current = current;
    }

    public void addNearby(User user){

        if(this.nearbyUsers.stream().noneMatch(u->u.getUsername().equals(user.getUsername()))){
            this.nearbyUsers.add(user);
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setState(Boolean state){
        this.state = state;
    }
    public void setCurrent(Position p){this.current = p;}

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public Boolean getState(){
        return state;
    }
    public Boolean getSpecial(){return special;}
    public Position getCurrent(){return current;}
    public List<User> getNearbyUsers() {
        return nearbyUsers;
    }

}