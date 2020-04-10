import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Rabbit implements IBehaviour {

    public static int AllQuantity = 0;
    protected double X, Y;
    protected BufferedImage img;

    protected void openImage(String pathname) {
        try {
            img = ImageIO.read(new File(pathname));
        } catch (IOException Ex) {
            System.out.println(Ex);
        }
    };

    public BufferedImage getImg() {
        return img;
    }

    Rabbit() { AllQuantity++; }

    Rabbit(double X, double Y) {
        AllQuantity++;
        this.X = X;
        this.Y = Y;
    }

    public static int getAllQuantity() {
        return AllQuantity;
    }

    @Override
    public void setX(double X) { this.X = X; }
    @Override
    public double getX() { return X; }
    @Override
    public void setY(double Y) { this.Y = Y; }
    @Override
    public double getY() { return Y; }

}
