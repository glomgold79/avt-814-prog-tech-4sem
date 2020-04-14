public class Ordinary extends Rabbit {

    public static int OrdinaryQuantity = 0;
    public static long TimeOfLife;

    Ordinary() { openImage("./images/Ordinary.png"); OrdinaryQuantity++; }
    Ordinary(double X, double Y, long BirthTime) {
        super(X, Y, BirthTime);
        openImage("./images/Ordinary.png");
        OrdinaryQuantity++;
    }

    public static int getOrdinaryQuantity() {
        return OrdinaryQuantity;
    }

}
