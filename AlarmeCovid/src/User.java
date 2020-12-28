import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private Boolean state; // Infected: True or False

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.state = null;
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

    public void serialize (DataOutputStream out) throws IOException {
        out.writeUTF((this.username));
        out.writeUTF(this.password);
        out.flush();
    }

    public static User deserialize (DataInputStream in) throws IOException{
        String username = in.readUTF();
        String password = in.readUTF();

        return new User(username,password);
    }

}