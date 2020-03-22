import java.awt.*;

abstract class Bee implements IBehaviour{
    int x, y;
    Image img;
    int id;
    long timeOfBirth;
    public void move(){}
    public void setX(int x){ this.x = x;}
    public void setY(int y){ this.y = y;}
    @Override
    public int getX(){ return x; }
    @Override
    public int getY(){ return y; }
}