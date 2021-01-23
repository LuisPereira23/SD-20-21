import java.io.*;

public class ObjectStream implements IObjectStream {

    public CovidAlarm loadServer() {
        String fileName = "src/Data/ServerData.dat";
        File f = new File(fileName);

        try {
            if(f.exists()) {
            FileInputStream file = new FileInputStream(fileName);
            ObjectInputStream out= new ObjectInputStream(file);
                Object a = out.readObject();
                return (CovidAlarm) a;
            }
        } catch (IOException |ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new CovidAlarm(new UserMap());
    }

    public void saveServer(CovidAlarm covid) {
        String fileName = "src/Data/ServerData.dat";
        File f = new File(fileName);
        try {
            f.createNewFile();
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out= new ObjectOutputStream(file);
            out.writeObject(covid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}