import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class Packet implements Serializable {
    int option;
    private String username;
    private String password;
    Boolean state;

    public Packet(int option,String username, String password,Boolean state) {
        this.option = option;
        this.username = username;
        this.password = password;
        this.state = state;
    }

    public int getOption() {
        return option;
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
        out.writeInt(this.option);
        out.writeUTF((this.username));
        out.writeUTF(this.password);
        out.writeBoolean(this.state);
        out.flush();
    }

    public static Packet deserialize (DataInputStream in) throws IOException{
        int option = in.readInt();
        String username = in.readUTF();
        String password = in.readUTF();
        Boolean state = in.readBoolean();

        return new Packet(option,username,password,state);
    }

}
