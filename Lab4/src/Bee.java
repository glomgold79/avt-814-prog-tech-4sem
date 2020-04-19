
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

abstract public class Bee implements IBehaviour {
    int x, y;
    Image img;
    int id;
    long timeOfBirth;
    int V;

    public void move() {
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    void setID(int id) {
        this.id = id;
    }
}
