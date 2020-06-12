package app;

import javax.swing.*;
import java.util.Random;

public class Drone extends Bee {
    private int Vx,
                Vy,
                rx,ry;
    private boolean
            end =true,
            borderX=true,
            borderY=true;

    Drone(ImageIcon img, int x, int y, long timeOfBirth) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.timeOfBirth = timeOfBirth;
   //     this.id=id;
        V = 1; // скорость, пикселя в секунду
        Vx=V;
        Vy=V;
    }

    @Override
    public void move() {
        if (end == true)
        {
            rx = new Random().nextInt(2);
            ry = new Random().nextInt(2);
            if (rx == 0) Vx *= -1;
            if (ry == 0) Vy *= -1;
            end = false;

        }
        if (borderX) {
            if (Vx > 0) {
                if (this.x <= Habitat.areaSizeX) {
                    this.x += Vx;
                } else borderX = false;
            }
            if (Vx < 0) {
                if (this.x >= 0) {
                    this.x += Vx;
                } else borderX = false;
            }
        }

        if (borderY) {
            if (Vy > 0) {
                if (this.y <= Habitat.areaSizeY) {
                    this.y += Vy;
                } else borderY = false;
            }
            if (Vy < 0) {
                if (this.y >= 0) {
                    this.y += Vy;
                } else borderY = false;
            }
        }

        if ( borderX==false && borderY==false) {

            if (Vx < 0) {
                if (this.x <= Habitat.areaSizeX/2) {
                    this.x -= Vx;
                }
            }

            if (Vx > 0) {
                if (this.x >= Habitat.areaSizeX/2) {
                    this.x -= Vx;
                }
            }

            if (Vy < 0) {
                if (this.y <= Habitat.areaSizeY/2) {
                    this.y -= Vy;
                }
            }

            if (Vy > 0) {
                if (this.y >= Habitat.areaSizeY/2) {
                    this.y -= Vy;
                }
            }
        }

        if (((Vy < 0 && this.y >= Habitat.areaSizeY / 2) || (Vy > 0 && this.y <= Habitat.areaSizeY / 2)) &&
                ((Vx < 0 && this.x >= Habitat.areaSizeX / 2) || (Vx > 0 && this.x <= Habitat.areaSizeX / 2))) {
            borderY =true;
            borderX = true;
            end=true;
        }

    }
}


