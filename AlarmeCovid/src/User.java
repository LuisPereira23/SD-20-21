import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private Boolean state; // Infected: True or False

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.state = false;
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