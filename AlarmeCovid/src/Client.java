import java.io.*;
import java.net.Socket;
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
        mainMenu(socket);
    }

    public static void mainMenu(Socket socket) throws IOException{
        System.out.println("\n****** Main Menu ******\n");
        System.out.println("1 - Login");
        System.out.println("2 - Registar Utilizador");
        System.out.println("0 - Sair");
        String option = in.next();

        switch (option) {
            case "1" -> userLogin(socket);
            case "2" -> userRegister(socket);
            case "0" -> close(socket);
            default -> {
                System.out.println("Opção Inválida.");
                in.nextLine();
                in.nextLine();
                mainMenu(socket);
            }
        }
    }

    public static void userLogin(Socket socket) throws IOException{
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


        if (a.charAt(0) == 'A') {
            newPacket = new Packet(9,user,pass,false,false,0,0);
            newPacket.serialize(output);
            String s  = input.readUTF();
            Boolean special = Boolean.parseBoolean(s);
            userMenu(user, pass, special, socket);
        }else {
            mainMenu(socket);
        }
    }

    public static void userRegister(Socket socket) throws IOException{
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
                userMenu(user,pass,special,socket);
            else
                mainMenu(socket);
        } else {
            System.out.println("Error defining location");
            mainMenu(socket);
        }
    }

    public static void userMenu(String username, String pass, Boolean special,Socket socket) throws IOException {
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
                case "1" -> localInfo(username, pass, true,socket);
                case "2" -> reportCovid(username, pass,socket);
                case "3" -> getNumber(username, pass, true,socket);
                case "4" -> changePosition(username, pass,true,socket);
                case "5" -> checkStateManual(username, pass,true,socket);
                case "6" -> printMap(username, pass,true,socket);
                case "0" -> mainMenu(socket);
                default -> {
                    System.out.println("Opção Inválida.");
                    in.nextLine();
                    in.nextLine();
                    userMenu(username, pass, true,socket);
                }
            }
        }else {
            switch (option) {
                case "1" -> localInfo(username, pass,false,socket);
                case "2" -> reportCovid(username, pass,socket);
                case "3" -> getNumber(username, pass, false,socket);
                case "4" -> changePosition(username, pass, false,socket);
                case "5" -> checkStateManual(username, pass, false,socket);
                case "0" -> mainMenu(socket);
                default -> {
                    System.out.println("Opção Inválida.");
                    in.nextLine();
                    in.nextLine();
                    userMenu(username, pass, false,socket);
                }
            }

        }

    }


    public static void reportCovid(String username, String pass,Socket socket) throws IOException {

        Packet newPacket = new Packet(3,username,pass,true,false,99999,99999);
        newPacket.serialize(output);
        System.out.println(input.readUTF());
        mainMenu(socket);
    }

    public static void changePosition(String user,String pass,Boolean special,Socket socket) throws IOException {
        in.nextLine();
        System.out.println("New Position (x y)");
        String ioPosition = in.nextLine();
        String[] position = ioPosition.split(" ");

        if(position.length ==2){
            try {
                int m = Integer.parseInt(position[0].trim());
                int n = Integer.parseInt(position[1].trim());

                Packet newPacket = new Packet(4, user, pass, false, special, m, n);
                newPacket.serialize(output);
                String s = input.readUTF();
            }catch (NumberFormatException e){System.out.println("Invalid format " +e.getMessage());}
        }else{System.out.println("Invalid Location");}
        userMenu(user,pass,special,socket);
    }

    public static void localInfo(String user, String pass,Boolean special,Socket socket) throws IOException {
        in.nextLine();
        System.out.println("Location to check (x y)");
        String ioPosition = in.nextLine();
        String[] position = ioPosition.split(" ");

        if(position.length ==2){
            try {
                int m = Integer.parseInt(position[0].trim());
                int n = Integer.parseInt(position[1].trim());

                Packet newPacket = new Packet(5, user, pass, false, special, m, n);
                newPacket.serialize(output);
                String s = input.readUTF();
                if (s.equals("false")) {
                    System.out.println("Safe location. Empty position");
                } else {
                    System.out.println("Location not safe");
                }
            }catch (NumberFormatException e){System.out.println("Invalid Format "+e.getMessage());}
        }else{System.out.println("Invalid Location");}
        userMenu(user,pass,special,socket);
    }

    public static void getNumber(String user,String pass,Boolean special,Socket socket) throws IOException {
        in.nextLine();
        System.out.println("Location to check (x y)");
        String ioPosition = in.nextLine();
        String[] position = ioPosition.split(" ");

        if(position.length ==2){
            try {
                int m = Integer.parseInt(position[0].trim());
                int n = Integer.parseInt(position[1].trim());

                Packet newPacket = new Packet(6, user, pass, false, special, m, n);
                newPacket.serialize(output);
                String s = input.readUTF();
                System.out.println("Number of people: " + s);
            }catch (NumberFormatException e){System.out.println("Invalid Format " +e.getMessage());}
        }else{System.out.println("Invalid Location");}
        userMenu(user,pass,special,socket);
    }

    public static void checkStateManual(String user,String pass,Boolean special,Socket socket) throws IOException {
        Packet newPacket = new Packet(7,user,pass,false,special,0,0);
        newPacket.serialize(output);
        String s = input.readUTF();
        System.out.println("Could i be infected? " + s);
        userMenu(user,pass,special,socket);
    }

    public static String checkStateAuto(String user,String pass) throws IOException {
        Packet newPacket = new Packet(7,user,pass,false,false,0,0);
        newPacket.serialize(output);
        String s = input.readUTF();
        return s;
    }

    public static void printMap(String user,String pass,Boolean special,Socket socket) throws IOException {
        Packet newPacket = new Packet(8,user,pass,false,false,0,0);
        newPacket.serialize(output);
        String s = input.readUTF();
        System.out.println(s);
        userMenu(user,pass,special,socket);
    }

    public static void close(Socket socket) throws IOException {
        socket.close();
        System.exit(0);
    }

}