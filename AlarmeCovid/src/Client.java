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

        System.out.println("Utilizador é admin? (y/n)");
        String IOspecial = in.nextLine();
        Boolean special = IOspecial.equals("y") || IOspecial.equals("Y");

        System.out.println("Insira as suas coordenadas atuais.");
        System.out.println("X:");
        String x = in.nextLine();
        System.out.println("Y:");
        String y = in.nextLine();

        if(x.chars().allMatch(Character::isDigit) && y.chars().allMatch(Character::isDigit)){

            Packet newPacket = new Packet(2,user,pass,false,special,Integer.parseInt(x),Integer.parseInt(y));
            newPacket.serialize(output);
            String auth = input.readUTF();
            System.out.println(auth); // informa se o registo foi bem sucedido
            in.nextLine();
            if(auth.charAt(0) == 'R')
                userMenu(user,pass,special,socket);
            else
                mainMenu(socket);
        } else {
            System.out.println("Coordenadas inválidas.");
            in.nextLine();
            mainMenu(socket);
        }
    }

    public static void userMenu(String username, String pass, Boolean special,Socket socket) throws IOException {

        if(!special){
            System.out.println("\n****** User Menu ******");
        }else{
            System.out.println("\n****** Admin Menu ******");
        }

        System.out.println("[ Bem-vindo " + username + " ]\n");
        System.out.println(checkState(username,pass));

        System.out.println("\n1 - Consultar dados de uma localização");
        System.out.println("2 - Comunicar caso de infeção");
        System.out.println("3 - Atualizar localização");

        if (special) System.out.println("4 - Consultar Mapa");

        System.out.println("5 - Atualizar informações do menu");
        System.out.println("0 - Sair");
        String option = in.next();

        switch (option) {
            case "1" -> localInfo(username, pass, special, socket);
            case "2" -> reportCovid(username, pass, socket);
            case "3" -> changePosition(username, pass, special, socket);
            case "4" -> {
                if (special) printMap(username, pass, special, socket);
                else userDefaults(username, pass, special, socket);
            }
            case "5" -> userMenu(username, pass, special, socket);
            case "0" -> mainMenu(socket);
            default -> userDefaults(username, pass, special, socket);
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
        System.out.println("Insira as suas novas coordenadas.");
        System.out.println("X:");
        String x = in.nextLine();
        System.out.println("Y:");
        String y = in.nextLine();

        if(x.chars().allMatch(Character::isDigit) && y.chars().allMatch(Character::isDigit)){

            Packet newPacket = new Packet(4, user, pass, false, special, Integer.parseInt(x), Integer.parseInt(y));
            newPacket.serialize(output);
            System.out.println(input.readUTF());

        }
        else System.out.println("Coordenadas inválidas.");
        userMenu(user,pass,special,socket);
    }

    public static void localInfo(String user, String pass,Boolean special,Socket socket) throws IOException {
        in.nextLine();

        System.out.println("Insira as coordenadas que pretende consultar.");
        System.out.println("X:");
        String x = in.nextLine();
        System.out.println("Y:");
        String y = in.nextLine();

        if(x.chars().allMatch(Character::isDigit) && y.chars().allMatch(Character::isDigit)){
            int m = Integer.parseInt(x);
            int n = Integer.parseInt(y);
            Packet newPacket = new Packet(5, user, pass, false, special, m, n);
            newPacket.serialize(output);
            String a = input.readUTF();
            System.out.println(a);

            //se nao existir ninguem na localização
            if(a.charAt(0) == 'N'){
                System.out.println("Deseja deslocar-se para esta localização? (y/n)");
                String yes = in.nextLine();
                if (yes.equals("y") || yes.equals("Y")){
                    Packet newPacket2 = new Packet(4, user, pass, false, special, m, n);
                    newPacket2.serialize(output);
                    System.out.println(input.readUTF());
                }
                else System.out.println("Localização não alterada.");
            }
        }
        else System.out.println("Coordenadas inválidas.");
        userMenu(user,pass,special,socket);
    }

    public static void printMap(String user,String pass,Boolean special,Socket socket) throws IOException {
        Packet newPacket = new Packet(8,user,pass,false,false,0,0);
        newPacket.serialize(output);
        System.out.println(input.readUTF());
        in.nextLine();
        in.nextLine();
        userMenu(user,pass,special,socket);
    }

    public static String checkState(String user,String pass) throws IOException {
        Packet newPacket = new Packet(7,user,pass,false,false,0,0);
        newPacket.serialize(output);
        return input.readUTF();
    }

    public static void userDefaults(String username, String pass, Boolean special,Socket socket) throws IOException {
        System.out.println("Opção Inválida.");
        in.nextLine();
        in.nextLine();
        userMenu(username, pass, special, socket);
    }

    public static void close(Socket socket) throws IOException {
        socket.close();
        System.exit(0);
    }

}