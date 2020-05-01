import java.io.IOException;
import java.io.PipedReader;

public class Console extends Thread {

    PipedReader pipedReader;
    int N;

    Console () {
        pipedReader = new PipedReader();
    }

    @Override
    public void run() {
        super.run();
        char[] buf = new char[100];
        try {
            pipedReader.read(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String string = new StringBuilder().append(buf).toString();
        if (string.substring(0, 9).equals("Cut back ")) {
            int len = string.indexOf("\n");
            if (len == 10) {
                N = Integer.parseInt(string.substring(9,10));
            }
            if (len == 11) {
                N = Integer.parseInt(string.substring(9,11));
            }
            if (len == 12) {
                N = 100;
            }
        }
        double persent = (100 - ((double) N)) / 100 * Albino.AlbinoQuantity;

        synchronized (Singleton.getVector()) {
            int i = 0;
            while (Albino.AlbinoQuantity != (int) persent && Singleton.getVector().size() != i) {
                if (Singleton.getVector().get(i) instanceof Albino) {
                    Rabbit.AllQuantity--;
                    Albino.AlbinoQuantity--;
                    Singleton.getHashMap().remove(Singleton.getVector().get(i).ID);
                    Singleton.getTreeSetID().remove(Singleton.getVector().get(i).ID);
                    Singleton.getVector().remove(i);
                }
                i++;
            }
        }
    }

    public void Start() {
        run();
    }

}
