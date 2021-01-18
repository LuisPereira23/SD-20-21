import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class AdminMenu {

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
        System.out.println("\n****** Admin Menu ******\n");
        System.out.println("1 - Registar Utilizador Especial ");
        System.out.println("2 - Consultar Contas");
        System.out.println("3 - Consultar Mapa");
        System.out.println("4 - Gravar Server");
        System.out.println("0 - Sair");
        String option = in.next();

        switch (option) {
            case "1" -> userRegister(socket);
            case "2" -> accountsList(socket);
            case "3" -> printMap(socket);
            case "4" -> saveServer(socket);
            case "0" -> close(socket);
            default -> {
                System.out.println("Opção Inválida.");
                in.nextLine();
                in.nextLine();
                mainMenu(socket);
            }
        }
    }


    public static void userRegister(Socket socket) throws IOException{
        System.out.println("-- Registar Utilizador Especial --");
        in.nextLine();
        System.out.println("Username:");
        String user = in.nextLine();
        if(user.isBlank()){
            System.out.println("Username não pode ser vazio");
            mainMenu(socket);
        }
        System.out.println("Password:");
        String pass = in.nextLine();
        if(pass.isBlank()){
            System.out.println("Password não pode ser vazia");
            mainMenu(socket);
        }
        System.out.println("Insira as suas coordenadas atuais. (x y)");
        String ioPosition = in.nextLine();
        String[] position = ioPosition.split(" ");
        if(position.length ==2){
            try {
                int m = Integer.parseInt(position[0].trim());
                int n = Integer.parseInt(position[1].trim());

                Packet newPacket = new Packet(2, user, pass, false, true, m, n);
                newPacket.serialize(output);
                String auth = input.readUTF();
                System.out.println(auth); // informa se o registo foi bem sucedido
                mainMenu(socket);
            }catch (NumberFormatException e){System.out.println("Formato Inválido " +e.getMessage());}
        } else
            {
            System.out.println("Coordenadas inválidas.");
            mainMenu(socket);
        }
    }

    public static void printMap(Socket socket) throws IOException {
        Packet newPacket = new Packet(7,"admin","admin",false,false,0,0);
        newPacket.serialize(output);
        String s = input.readUTF();
        System.out.println(s);
        System.out.println("\nEnter para continuar ");
        in.nextLine();
        in.nextLine();
        mainMenu(socket);
    }

    public static void saveServer(Socket socket) throws IOException {
        Packet newPacket = new Packet(9, "admin", "admin", false, true, 99999, 99999);
        newPacket.serialize(output);
        String auth = input.readUTF();
        System.out.println(auth);
        System.out.println("\nEnter para continuar ");
        in.nextLine();
        in.nextLine();
        mainMenu(socket);
    }

    public static void accountsList(Socket socket) throws IOException {
        Packet newPacket = new Packet(10,"admin","admin",false,false,0,0);
        newPacket.serialize(output);
        String s = input.readUTF();
        System.out.println(s);
        System.out.println("\nEnter para continuar ");
        in.nextLine();
        in.nextLine();
        mainMenu(socket);
    }

    public static void close(Socket socket) throws IOException {
        socket.close();
        System.exit(0);
    }

}