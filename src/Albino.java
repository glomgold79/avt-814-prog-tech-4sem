public class Albino extends Rabbit {

    public static int AlbinoQuantity = 0;
    public static long TimeOfLife;

    Albino() { openImage("./images/Albino.png"); AlbinoQuantity++; }
    Albino(double X, double Y, long BirthTime) {
        super(X, Y,BirthTime);
        openImage("./images/Albino.png");
        AlbinoQuantity++;
    }

    public static int getAlbinoQuantity() {
        return AlbinoQuantity;
    }

}
