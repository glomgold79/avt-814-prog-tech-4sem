package com.company;

import java.awt.image.BufferedImage;

abstract class card_index implements IBehaviour {

    public double x, y, V = 0.01, startx, starty, x2, y2;
    int timespawn, id;
    boolean go = true;
    BufferedImage image;

    public void set_to_point(double x, double y){
        this.x2 = x;
        this.y2 = y;
    }

    public void Move(){

    };
    public double GetX(){
        return x;
    };
    public double GetY(){
        return y;
    };
    public void SetX(){};
    public void SetY(){};

    card_index(){
        this.x = 0;
        this.y = 0;
    }

}
