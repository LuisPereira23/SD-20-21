import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Client {

    private final static int port = 34567;
    private final static String ip = "localhost";
    private static DataOutputStream output = null;
    private static DataInputStream input = null;
    public static Scanner in = new Scanner(System.in);

    public static void main(String[] args) throws Exception{
        Socket socket = new Socket(ip, port);
        output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
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
                System.out.println("Opção Inválida.");
                in.nextLine();
                in.nextLine();
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

        Packet newPacket = new Packet(1,user,pass,false);
        newPacket.serialize(output);

        String s = input.readUTF();
        System.out.println(s); // informa se o login foi bem sucedido
        in.nextLine();

        if (s.charAt(0) == 'A')
            userMenu(user, pass);
        else
            mainMenu();
    }

    public static void userRegister() throws IOException{
        System.out.println("-- Registar Utilizador --");
        in.nextLine();
        System.out.println("Username:");
        String user = in.nextLine();
        System.out.println("Password:");
        String pass = in.nextLine();

        Packet newPacket = new Packet(2,user,pass,false);
        newPacket.serialize(output);

        System.out.println(input.readUTF()); // informa se o registo foi bem sucedido
        in.nextLine();
        mainMenu();
    }

    public static void userMenu(String username, String pass) throws IOException {
        System.out.println("\n****** User Menu ******");
        System.out.println("[ Bem-vindo " + username + " ]\n");
        System.out.println("1 - Consultar dados de uma localização");
        System.out.println("2 - Comunicar infeção");
        //System.out.println("3 - etc...");
        System.out.println("0 - Sair");
        String option = in.next();

        switch (option) {
            case "1" -> localInfo();
            case "2" -> reportCovid(username, pass);
            //case "3" -> ...
            case "0" -> mainMenu();
            default -> {
                System.out.println("Opção Inválida.");
                in.nextLine();
                in.nextLine();
                userMenu(username, pass);
            }
        }
    }

    public static void localInfo(){

    };

    public static void reportCovid(String username, String pass) throws IOException {

        Packet newPacket = new Packet(3,username,pass,true);
        newPacket.serialize(output);

        System.out.println(input.readUTF());
        in.nextLine();
        in.nextLine();
        mainMenu();
    };

}