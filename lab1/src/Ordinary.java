import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Ordinary extends Rabbit implements Serializable {

    public static int OrdinaryQuantity = 0;
    public static long TimeOfLife = 200;
    public int targetX = Habitat.gui.jPanelImage.getWidth() / 2, targetY = Habitat.gui.jPanelImage.getHeight() / 2;
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

    Ordinary(int X, int Y, long BirthTime) {
        super(X, Y, BirthTime);
        openImage("./images/Ordinary.png");
        OrdinaryQuantity++;
    }

    public static int getOrdinaryQuantity() {
        return OrdinaryQuantity;
    }

}
