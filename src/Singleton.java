import java.util.HashMap;
import java.util.TreeSet;
import java.util.Vector;

public class Singleton {
    private Singleton(){}
    private static Vector<Rabbit> vector = new Vector<>();
    public static Vector<Rabbit> getVector() { return vector; }
    private static TreeSet<Long> treeSetID = new TreeSet<>();
    public static TreeSet<Long> getTreeSetID() {return treeSetID; }
    private static HashMap<Long, Long> hashMap = new HashMap<>();
    public static HashMap<Long, Long> getHashMap() { return hashMap; }
}
