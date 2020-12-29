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
    public static Scanner in = new Scanner(System.in);

    public static void main(String args[]) throws Exception{
        socket = new Socket(ip,port);
        mainMenu();
    }

    public static void mainMenu() throws IOException{
        System.out.println("\n****** Main Menu ******\n");
        System.out.println("1 - Login");
        System.out.println("2 - Registar Utilizador");
        System.out.println("0 - Sair");
        String option = in.next();

        switch (option) {
            case "1" -> userLogin();
            case "2" -> userRegister();
            case "0" -> System.exit(0);
            default -> {
                System.out.println("Invalid Option.\n");
                mainMenu();
            }
        }
    }

    public static void userLogin() throws IOException{
        System.out.println("-- Login --");
        in.nextLine();
        System.out.println("Username:");
        String user = in.nextLine();
        System.out.println("Password:");
        String pass = in.nextLine();

        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        Packet newPacket = new Packet(1,user,pass,false);
        newPacket.serialize(out);

        DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        Boolean auth = in.readBoolean();
        System.out.println(auth); //debug

        if (auth) userMenu(user);
        else{
            System.out.println("Failed Authentication.\n");
            mainMenu();
        }
    }

    public static void userRegister() throws IOException{
        System.out.println("-- Register --");
        in.nextLine();
        System.out.println("Username:");
        String user = in.nextLine();
        System.out.println("Password:");
        String pass = in.nextLine();

        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

        Packet newPacket = new Packet(2,user,pass,false);
        newPacket.serialize(out);

        System.out.println("Registered.\n");
        mainMenu();
    }

    public static void userMenu(String username) throws IOException {
        System.out.println("\n****** User Menu ******");
        System.out.println("[ Bem-vindo " + username + " ]\n");
        System.out.println("1 - Consultar dados de uma localização");
        System.out.println("2 - Comunicar infeção");
        //System.out.println("3 - etc...");
        System.out.println("0 - Sair");
        String option = in.next();

        switch (option) {
            case "1" -> localInfo();
            case "2" -> reportCovid(username);
            //case "3" -> ...
            case "0" -> {
                //logout(user);
                mainMenu();
            }
            default -> {
                System.out.println("Invalid Option.\n");
                userMenu(username);
            }
        }
    }


    public static void localInfo(){

    };

    public static void reportCovid(String username){

    };


}