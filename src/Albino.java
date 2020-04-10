public class Albino extends Rabbit {

    public static int AlbinoQuantity = 0;

    Albino() { openImage("./images/Albino.png"); AlbinoQuantity++; }
    Albino(double X, double Y) {
        super(X, Y);
        openImage("./images/Albino.png");
        AlbinoQuantity++;
    }

    public static int getAlbinoQuantity() {
        return AlbinoQuantity;
    }

}
