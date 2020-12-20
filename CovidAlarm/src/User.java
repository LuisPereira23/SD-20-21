import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private Boolean state; // Infected: True or False

    public User() {
        this.username = "";
        this.password = "";
        this.state = false;
    }

    public User(String s, String p) {
        this.username = s;
        this.password = p;
        this.state = false;
    }

    public User(User u) {
        this.username = u.getUsername();
        this.password = u.getPassword();
        this.state = u.getState();
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setState(Boolean state){
        this.state=true;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public Boolean getState(){
        return state;
    }

}