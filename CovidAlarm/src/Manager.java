import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Manager implements Runnable {
    private final Socket socket;
    private final CovidAlarm covidAlarm;
    private ObjectInputStream input;
    private PrintWriter output;

    public Manager (Socket client, CovidAlarm covidAlarm){
        this.socket = client;
        this.covidAlarm = covidAlarm;
        this.input = null;
        this.output = null;
    }

    public void run() {
        try{
            do {
                this.input = new ObjectInputStream(socket.getInputStream());
                this.output = new PrintWriter(socket.getOutputStream());

                Packet packet = (Packet) input.readObject();

                if (packet.getOption().equals(Server.Register_User)) {
                    System.err.println("Recebeu Registar Utilizador");
                    String username = packet.getArg().get(Server.User_Name);
                    String password = packet.getArg().get(Server.User_Password);

                    boolean exists = covidAlarm.userRegister(username, password);

                    if(exists){
                        output.println("Registered with sucess");
                    }else{
                        output.println("User already exists");
                    }
                    output.flush();
                }
                if (packet.getOption().equals(Server.Login_User)){
                    System.err.println("Pacote Entrar");
                    String user = packet.getArg().get(Server.User_Name);
                    String pass = packet.getArg().get(Server.User_Password);

                    boolean state = false;

                    if(covidAlarm.getUser().containsKey(user)){
                        state = covidAlarm.getUser().get(user).getState();
                    }

                    boolean auth = covidAlarm.AuthUser(user, pass);

                    if(auth && !state){
                        output.println("Authenticated");
                    }else{
                        output.println("Not Authenticated");
                    }
                    output.flush();
                }
            }while(true);
        }catch (Exception e) {
        }
    }

}
