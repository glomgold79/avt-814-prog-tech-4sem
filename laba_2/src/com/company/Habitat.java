package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;


public class Habitat {
    double P1 = 0.7, P2 = 0.3;
    int N1 = 5, N2 = 7;

    public void setProbabilityPerson(double P){
        this.P1 = P;
        System.out.println("person probability = " + this.P1);
    }
    public void setProbabilityLaw(double P){
        this.P2 = P;
        System.out.println("law probability = " + this.P2);
    }
    public void setPeriodPerson(int t){
        this.N1 = t;
        System.out.println("Period generation person " + N1);
    }
    public void setPeriodLaw(int t){
        this.N2 = t;
        System.out.println("Period generation law " + N2);
    }

    private BufferedImage PersonImage;
    private BufferedImage lawImage;

    void Update(long time) {
        if (time % N1 == 0) {
            double x = Math.random();
            if (x <= P1) {
                Person A = new Person(PersonImage, Math.random(),Math.random());
                MySingleton.pack.numbersPerson++;
                MySingleton.pack.add(A);
                System.out.println("Появился новый налогоплательщик!");
            }
        }
        if (time % N2 == 0) {
            double x = Math.random();
            if (x <= P2) {
                law A = new law(lawImage, Math.random(), Math.random());
                MySingleton.pack.numdersLaw++;
                MySingleton.pack.add(A);
                System.out.println("Основана новая фирма!");
            }
        }
    }

    Habitat(){

        try{
            PersonImage = ImageIO.read(getClass().getResourceAsStream("fiz-face.png"));
        }catch (IOException e){
            e.printStackTrace();
        }

        try{
            lawImage = ImageIO.read(getClass().getResourceAsStream("law-face.png"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
