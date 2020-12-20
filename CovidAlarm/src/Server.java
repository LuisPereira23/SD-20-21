import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int port = 55000; // Connection port

    //Account management
    public static final String Register_User = "RegistarUtilizador";
    public static final String User_Name = "NomeUtilizador";
    public static final String User_Password = "PasswordUtilizador";
    public static final String Login_User = "EntrarUtilizador";
    public static final String Logout_User = "SairUtilizador";

    public static void main(String args[]) throws IOException{
        ServerSocket ws = new ServerSocket(port);

        CovidAlarm covidAlarm = new CovidAlarm();

        while(true) {
            Socket user = ws.accept();
            System.out.println("Entered the server\nIP: " + user.getInetAddress());

            new Thread(new Manager(user, covidAlarm)).start();
        }
    }
}


