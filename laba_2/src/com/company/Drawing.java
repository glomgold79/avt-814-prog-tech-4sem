package com.company;

import javax.swing.*;
import java.awt.*;


public class Drawing extends JPanel {
    int wigth, heidht;

    Drawing(int x, int y){
        setPreferredSize(new Dimension(x, y));
        setLayout(new FlowLayout(FlowLayout.CENTER));
        this.wigth = x - 200;
        this.heidht = y - 200;
    }

    void draw(Graphics Graph){

        for(int i= 0; i < MySingleton.pack.sizeArray; i++){
            Graph.drawImage(MySingleton.pack.Array[i].image,(int)(MySingleton.pack.Array[i].x*wigth+100),(int) (MySingleton.pack.Array[i].y*heidht+100),null);
        }
    }
}
