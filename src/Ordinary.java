public class Ordinary extends Rabbit {

    public static int OrdinaryQuantity = 0;

    Ordinary() { openImage("./images/Ordinary.png"); OrdinaryQuantity++; }
    Ordinary(double X, double Y) {
        super(X, Y);
        openImage("./images/Ordinary.png");
        OrdinaryQuantity++;
    }

    public static int getOrdinaryQuantity() {
        return OrdinaryQuantity;
    }

}
