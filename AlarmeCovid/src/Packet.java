import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class Packet implements Serializable {
    private final int option;
    private final String username;
    private final String password;
    private final Boolean state;
    private final Boolean special;
    private final int m;
    private final int n;

    public Packet(int option, String username, String password, Boolean state,Boolean special,int m,int n) {
        this.option = option;
        this.username = username;
        this.password = password;
        this.state = state;
        this.special = special;
        this.m = m;
        this.n = n;
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
    public Boolean getSpecial(){return special;}
    public int getM(){return m;}
    public int getN(){return n;}

    public void serialize (DataOutputStream out) throws IOException {
        out.writeInt(this.option);
        out.writeUTF((this.username));
        out.writeUTF(this.password);
        out.writeBoolean(this.state);
        out.writeBoolean(this.special);
        out.writeInt(this.m);
        out.writeInt(this.n);
        out.flush();
    }

    public static Packet deserialize (DataInputStream in) throws IOException{
        int option = in.readInt();
        String username = in.readUTF();
        String password = in.readUTF();
        Boolean state = in.readBoolean();
        Boolean special = in.readBoolean();
        int m = in.readInt();
        int n = in.readInt();

        return new Packet(option,username,password,state,special,m,n);
    }

}
