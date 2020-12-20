import java.io.Serializable;
import java.util.HashMap;

public class Packet implements Serializable {
    private final String option;
    private final HashMap<String,String> args;


    public Packet(String option,HashMap<String, String> args) {
        this.option = option;
        this.args = args;
    }
    public String getOption(){
        return option;
    }

    public HashMap<String,String> getArg(){
        return args;
    }
}
