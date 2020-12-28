import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Client {

    private final static int port = 34567;
    private final static String ip = "localhost";
    private static Socket socket;
    public static String username = null;

    public static Scanner in = new Scanner(System.in);
    public static DataOutputStream output = null;


    public static void main(String args[]) throws Exception{
        socket = new Socket(ip,port);
        menu();
    }

    public static User parseLine (String userInput) {
        String[] tokens = userInput.split(" ");

        return new User(
                tokens[0],
                tokens[1]);
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

            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            out.writeInt(1);
            out.flush();
            String userInput = user + " " + pass;
            User newUser = parseLine(userInput);
            newUser.serialize(out);

            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            Boolean auth = in.readBoolean();
            System.out.println(auth);
            if(auth){
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

                DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                out.writeInt(2);
                out.flush();
                String userInput = user + " " + pass;
                User newUser = parseLine(userInput);
                newUser.serialize(out);
            }
        }
    }

    public static void authMenu() {
        System.out.println("Menu");
        System.out.println("test");
        String option = in.next();

        do {
            if(option.equals("2")){
                //menu();
            }
            if(option.equals("1")){
                //  menu();
            }
            if(option.equals("3")) {
                // menu();
            }
            if(option.equals("4")){
                // logout();
            }
            else{
                if (!(option.equals("1") || option.equals("2") || option.equals("3")))
                    System.out.println("Invalid Option");
                authMenu();
            }
        } while(!(option.equals("1") || option.equals("2") || option.equals("3")));
    }


}