import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Albino extends Rabbit implements Serializable {

    public static int AlbinoQuantity = 0;
    public static long TimeOfLife;
    public boolean DirectionIsToRight = true;
    protected static BufferedImage img;

    @Override
    protected void openImage(String pathname) {
        try {
            img = ImageIO.read(new File(pathname));
        } catch (IOException ignored) { }
    }

    public static BufferedImage getImg() {
        return img;
    }

    Albino(int X, int Y, long BirthTime) {
        super(X, Y,BirthTime);
        openImage("./images/Albino.png");
        AlbinoQuantity++;
    }

    public static int getAlbinoQuantity() {
        return AlbinoQuantity;
    }

}
