import java.io.*;
import java.util.Vector;

public class Serializator {

    public void Serialization() {
        File file = new File("Objects.data");
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            for (Rabbit rabbit : Singleton.getVector())
                if (rabbit instanceof Ordinary)
                    objectOutputStream.writeObject((Ordinary) rabbit);
                else
                    objectOutputStream.writeObject((Albino) rabbit);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                objectOutputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  void Deserialization() {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            fileInputStream = new FileInputStream("Objects.data");
            objectInputStream = new ObjectInputStream(fileInputStream);
            Singleton.getVector().removeAllElements();
            try {
                while (true) { //очень некрасиво
                    Singleton.getVector().add((Rabbit) objectInputStream.readObject());
                    Singleton.getTreeSetID().add(Singleton.getVector().lastElement().ID);
                    Singleton.getHashMap().put(Singleton.getVector().lastElement().ID, Singleton.getVector().lastElement().BirthTime);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                objectInputStream.close();
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
