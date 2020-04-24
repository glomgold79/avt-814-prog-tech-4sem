public class Ordinary extends Rabbit {

    public static int OrdinaryQuantity = 0;
    public static long TimeOfLife;
    public int targetX, targetY;

    Ordinary() { openImage("./images/Ordinary.png"); OrdinaryQuantity++; }
    Ordinary(int X, int Y, long BirthTime) {
        super(X, Y, BirthTime);
        openImage("./images/Ordinary.png");
        OrdinaryQuantity++;
    }

    public static int getOrdinaryQuantity() {
        return OrdinaryQuantity;
    }

}
