package app;

import javax.swing.*;
import java.util.Random;


public class Worker extends Bee {
    long time;
    int  N;
    int direction;
    Worker(ImageIcon img, int x, int y, long timeOfBirth) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.timeOfBirth = timeOfBirth;
      //  this.id=id;
        V = 1; // скорость, пикселя в секунду
        N=1;//меняется направление раз в N секунд
        time=0;
    }

    @Override
    public void move() {
        time++;

        if (time % (N*100) == 0) {
        direction=new Random().nextInt(4);

        }
        if (direction == 0 && this.x <= Habitat.areaSizeX) {

                this.x += V;
        }
        if (direction == 1 && this.x > 0) {

            this.x -= V;
        }

        if (direction == 2 && this.y <= Habitat.areaSizeY) {

            this.y += V;
        }
        if (direction == 3 && this.y > 0) {

            this.y -= V;
        }
    }
}
