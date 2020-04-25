import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public abstract class Rabbit implements IBehaviour, Serializable {

    public static int AllQuantity = 0;
    private static long staticID = 0;
    protected int X, Y;
    protected long BirthTime;
    protected long ID;

    protected void openImage(String pathname) { }

    Rabbit(int X, int Y, long BirthTime) {
        ID = staticID++;
        AllQuantity++;
        this.X = X;
        this.Y = Y;
        this.BirthTime = BirthTime;
    }

    public static int getAllQuantity() {
        return AllQuantity;
    }

    @Override
    public void setX(int X) { this.X = X; }
    @Override
    public int getX() { return X; }
    @Override
    public void setY(int Y) { this.Y = Y; }
    @Override
    public int getY() { return Y; }

}
