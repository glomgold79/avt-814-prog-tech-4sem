package com.company;

import java.awt.image.BufferedImage;


public class Person extends card_index {

    @Override
    public void Move(){
        if(go) {
            if(Math.abs(x-x2) < V && Math.abs(y-y2) < V)
                go = false;

            double dx, dy, k = Math.sqrt((x2 - startx)*(x2 - startx) + (y2-starty)*(y2-starty));

            dx = (x2 - startx)/k;
            dy = (y2 - starty)/k;

            x+=dx*V;
            y+=dy*V;

            /*if(x > x2)
              x -= V;
            else
                x+=V;
            if(y > y2)
                y -= V;
            else
                y += V;*/

        }
    }

    Person(BufferedImage Img, double x, double y){
        this.x = x;
        this.startx = x;
        this.y = y;
        this.starty = y;
        this.image = Img;
    }
    Person(){

    }
}
