import java.util.ArrayList;

public class Singleton {
    private Singleton(){}
    private static ArrayList<Rabbit> link = new ArrayList<>();
    public static ArrayList<Rabbit> getLink() {
        return link;
    }
}
