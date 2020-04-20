package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.IndexColorModel;
import java.text.AttributedCharacterIterator;


public class Drawing extends JPanel {
    int wigth, heidht;

    Drawing(int x, int y){
        setPreferredSize(new Dimension(x, y));
        setLayout(new FlowLayout(FlowLayout.CENTER));
        this.wigth = x - 200;
        this.heidht = y - 200;
    }

    void draw(Graphics Graph){

        Image bufim = this.createImage(wigth+200,heidht+200);
        Graphics buf = bufim.getGraphics();

        for(int i= 0; i < MySingleton.pack.Array.size(); i++){
            buf.drawImage(MySingleton.pack.Array.get(i).image,(int)(MySingleton.pack.Array.get(i).x*wigth+100),(int) (MySingleton.pack.Array.get(i).y*heidht+100),null);
        }
        Graph.drawImage(bufim,0,0,null);

    }


}
