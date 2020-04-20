package com.company;

import java.awt.image.BufferedImage;

abstract class card_index implements IBehaviour {

    public double x, y;
    int timespawn, id;
    BufferedImage image;

    public void Move(){};
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
