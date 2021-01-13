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

        Packet newPacket = new Packet(1,user,pass,false,false,0,0);
        newPacket.serialize(output);
        String a = input.readUTF();
        System.out.println(a); // informa se o login foi bem sucedido

        newPacket = new Packet(9,user,pass,false,false,0,0);
        newPacket.serialize(output);
        String s  = input.readUTF();
        Boolean special = Boolean.parseBoolean(s);

        if (a.charAt(0) == 'A')
            userMenu(user, pass,special);
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
        System.out.println("Is special? (true/false)");
        String IOspecial = in.nextLine();
        Boolean special = Boolean.parseBoolean(IOspecial);
        System.out.println("Current Position (x y)");
        String ioPosition = in.nextLine();
        String[] position = ioPosition.split(" ");
        if(position.length ==2){
             int m = Integer.parseInt(position[0].trim());
             int n = Integer.parseInt(position[1].trim());

            Packet newPacket = new Packet(2,user,pass,false,special,m,n);
            newPacket.serialize(output);
            String auth = input.readUTF();
            System.out.println(auth); // informa se o registo foi bem sucedido
            if(auth.charAt(0) == 'R')
                userMenu(user,pass,special);
            else
                mainMenu();
        } else {
            System.out.println("Error defining location");
            mainMenu();
        }
    }

    public static void userMenu(String username, String pass, Boolean special) throws IOException {
        String state = checkStateAuto(username,pass);
        if(state.charAt(0) == 't'){
            state = "You might be infected";
        } else {
            state = "You haven't made contact with someone infected";
        }

        String menu;
        if(special){
            menu = "Admin";
        }else{
            menu = "User";
        }

        System.out.println("\n****** "+menu+" Menu ******");
        System.out.println("[ Bem-vindo " + username + " , " + state + " ]\n");
        System.out.println("1 - Consultar dados de uma localização");
        System.out.println("2 - Comunicar infeção");
        System.out.println("3 - Number of people in position");
        System.out.println("4 - Change Position");
        System.out.println("5 - Check my state");
        if(special){System.out.println("6 - Check map");}
        System.out.println("0 - Sair");
        String option = in.next();

        if(special) {
            switch (option) {
                case "1" -> localInfo(username, pass, true);
                case "2" -> reportCovid(username, pass);
                case "3" -> getNumber(username, pass, true);
                case "4" -> changePosition(username, pass,true);
                case "5" -> checkStateManual(username, pass,true);
                case "6" -> printMap(username, pass,true);
                case "0" -> mainMenu();
                default -> {
                    System.out.println("Opção Inválida.");
                    in.nextLine();
                    in.nextLine();
                    userMenu(username, pass, true);
                }
            }
        }else {
            switch (option) {
                case "1" -> localInfo(username, pass,false);
                case "2" -> reportCovid(username, pass);
                case "3" -> getNumber(username, pass, false);
                case "4" -> changePosition(username, pass, false);
                case "5" -> checkStateManual(username, pass, false);
                case "0" -> mainMenu();
                default -> {
                    System.out.println("Opção Inválida.");
                    in.nextLine();
                    in.nextLine();
                    userMenu(username, pass, false);
                }
            }

        }

    }


    public static void reportCovid(String username, String pass) throws IOException {

        Packet newPacket = new Packet(3,username,pass,true,false,99999,99999);
        newPacket.serialize(output);
        System.out.println(input.readUTF());
        mainMenu();
    }

    public static void changePosition(String user,String pass,Boolean special) throws IOException {
        in.nextLine();
        System.out.println("New Position (x y)");
        String ioPosition = in.nextLine();
        String[] position = ioPosition.split(" ");

        if(position.length ==2){
            int m = Integer.parseInt(position[0].trim());
            int n = Integer.parseInt(position[1].trim());

            Packet newPacket = new Packet(4,user,pass,false,special,m,n);
            newPacket.serialize(output);
            String s = input.readUTF();
        }else{System.out.println("Invalid Location");}
        userMenu(user,pass,special);
    }

    public static void localInfo(String user, String pass,Boolean special) throws IOException {
        in.nextLine();
        System.out.println("Location to check (x y)");
        String ioPosition = in.nextLine();
        String[] position = ioPosition.split(" ");

        if(position.length ==2){
            int m = Integer.parseInt(position[0].trim());
            int n = Integer.parseInt(position[1].trim());

            Packet newPacket = new Packet(5,user,pass,false,special,m,n);
            newPacket.serialize(output);
            String s = input.readUTF();
            System.out.println(s);
            if(s.equals("false")) {
                System.out.println("Safe location. Empty position");
            } else{
                System.out.println("Location not safe");
            }
        }else{System.out.println("Invalid Location");}
        userMenu(user,pass,special);
    }

    public static void getNumber(String user,String pass,Boolean special) throws IOException {
        in.nextLine();
        System.out.println("Location to check (x y)");
        String ioPosition = in.nextLine();
        String[] position = ioPosition.split(" ");

        if(position.length ==2){
            int m = Integer.parseInt(position[0].trim());
            int n = Integer.parseInt(position[1].trim());

            Packet newPacket = new Packet(6,user,pass,false,special,m,n);
            newPacket.serialize(output);
            String s = input.readUTF();
            System.out.println("Number of people: " + s);
        }else{System.out.println("Invalid Location");}
        userMenu(user,pass,special);
    }

    public static void checkStateManual(String user,String pass,Boolean special) throws IOException {
        Packet newPacket = new Packet(7,user,pass,false,special,0,0);
        newPacket.serialize(output);
        String s = input.readUTF();
        System.out.println("Could i be infected? " + s);
        userMenu(user,pass,special);
    }

    public static String checkStateAuto(String user,String pass) throws IOException {
        Packet newPacket = new Packet(7,user,pass,false,false,0,0);
        newPacket.serialize(output);
        String s = input.readUTF();
        return s;
    }

    public static void printMap(String user,String pass,Boolean special) throws IOException {
        Packet newPacket = new Packet(8,user,pass,false,false,0,0);
        newPacket.serialize(output);
        String s = input.readUTF();
        System.out.println(s);
        userMenu(user,pass,special);
    }

}