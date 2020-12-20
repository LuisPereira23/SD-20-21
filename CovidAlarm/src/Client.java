import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Client {
    private final static int port = 55000;
    private final static String ip = "localhost";
    private static Socket socket;

    public static Scanner in = new Scanner(System.in);
    public static ObjectOutputStream objOutput = null;
    public static ObjectInputStream objInput = null;
    public static HashMap<String, String> hash;
    public static String username = null;
    public static Packet data;
    public static OutputStream outStream = null;
    public static InputStream inStream = null;


    public static void main(String args[]) throws Exception{
        socket = new Socket(ip,port);
        menu();
    }

    public static String initialMenu(){
        System.out.println("Menu");
        System.out.println("Application #1");
        System.out.println("Exit #2");
        String option = in.next();

        if ( !(option.equals("1") || option.equals("2"))){
            option = initialMenu();
        }
        return option;
    }

    public static void menu() throws IOException, ClassNotFoundException{
        String option;
        option = initialMenu();

        do{
            if(option.equals("1")){
                authUser();
            }else if(option.equals("2")){
                System.exit(0);
            }
        }
        while(true);
    }

    public static String userMenu(){
        System.out.println("1 - Login");
        System.out.println("2 - Register");
        String option = in.next();

        if ( !(option.equals("1") || option.equals("2"))){
            option = userMenu();
        }

        return option;
    }


    public static void authUser() throws IOException{
        String option;

        option = userMenu();

        if(option.equals("1")){
            System.out.println("Login");
            in.nextLine();
            System.out.println("#Username:");
            String user = in.nextLine();
            System.out.println("password:");
            String pass = in.nextLine();


            hash = new HashMap<>();
            hash.put(Server.User_Name, user);
            hash.put(Server.User_Password, pass);
            data = new Packet(Server.Login_User,hash);


            objOutput = new ObjectOutputStream(socket.getOutputStream());
            objOutput.writeObject(data);
            objOutput.flush();

            BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String auth = socketInput.readLine();
            System.out.println(auth);
            if(auth.equals("Authenticated")){
                username = user;
                authMenu();
            }
            else{
                System.out.println("Failed Authentication\n");
            }
        }else{

            if(option.equals("2")){
                System.out.println("Register");
                in.nextLine();
                System.out.println("Username");
                String user = in.nextLine();
                System.out.println("Password");
                String pass = in.nextLine();

                hash = new HashMap<>();
                hash.put(Server.User_Name, user);
                hash.put(Server.User_Password, pass);

                data = new Packet(Server.Register_User,hash);

                objOutput = new ObjectOutputStream(socket.getOutputStream());
                objOutput.writeObject(data);
                objOutput.flush();

                BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("\n"+socketInput.readLine()+"\n");
            }
        }
    }

    public static void authMenu() {
        System.out.println("Menu");
        System.out.println("test");
        String option = in.next();

        do {
            if(option.equals("2")){
                //menuUploadMusica();
            }
            if(option.equals("1")){
              //  menuDownloadMusica();
            }
            if(option.equals("3")) {
               // menuMusicas();
            }
            if(option.equals("4")){
               // logoutUtili();
            }
            else{
                if (!(option.equals("1") || option.equals("2") || option.equals("3")))
                    System.out.println("Invalid Option");
                authMenu();
            }
        } while(!(option.equals("1") || option.equals("2") || option.equals("3")));
    }
}
