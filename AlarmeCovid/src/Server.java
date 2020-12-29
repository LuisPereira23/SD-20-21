import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class ServerWorker implements Runnable {
    private Socket socket;
    private CovidAlarm covidalarm;

    public ServerWorker (Socket socket, CovidAlarm covidAlarm) {
        this.socket = socket;
        this.covidalarm = covidAlarm;
    }

    // @TODO
    @Override
    public void run() {

        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            boolean isActive = true;

            while (isActive) {

                boolean auth = covidalarm.OptionChose(in);
                out.writeBoolean(auth);
                out.flush();
                System.out.println(covidalarm.convertWithStream());
            }

            socket.shutdownInput();
            socket.close();
            System.out.println("Connection closed ...");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


public class Server {

    public static void main (String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(34567);
        CovidAlarm covidAlarm = new CovidAlarm();

        while (true) {
            Socket socket = serverSocket.accept();
            Thread worker = new Thread(new ServerWorker(socket, covidAlarm));
            worker.start();
        }
    }
}