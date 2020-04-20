package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;


public class Habitat {
    double P1 = 0.7, P2 = 0.3;
    int N1 = 5, N2 = 7;

    int timelivePerson = 40, timeliveLaw = 90;

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

    public void setTimelivePerson(int timelive) {
        this.timelivePerson = timelive;
        System.out.println("Time Live = " + this.timelivePerson);
    }
    public void setTimeliveLaw(int timelive){
        this.timeliveLaw = timelive;
        System.out.println("time live law = " + timeliveLaw);
    }

    private BufferedImage PersonImage;
    private BufferedImage lawImage;

    void checkTime(long time){

        for(int i = 0; i< MySingleton.pack.Array.size(); i++){
            if(MySingleton.pack.Array.get(i).getClass() == new Person().getClass()){
                if(time - MySingleton.pack.Array.get(i).timespawn >= timelivePerson){
                    MySingleton.pack.Tree.remove(MySingleton.pack.Array.get(i).id);
                    MySingleton.pack.hash.remove(MySingleton.pack.Array.get(i).id);
                    MySingleton.pack.numbersPerson--;
                    MySingleton.pack.Array.remove(i);
                    System.out.println("delete Person");
                }
            }else{
                if(time - MySingleton.pack.Array.get(i).timespawn >= timeliveLaw){
                    MySingleton.pack.Tree.remove(MySingleton.pack.Array.get(i).id);
                    MySingleton.pack.hash.remove(MySingleton.pack.Array.get(i).id);
                    MySingleton.pack.numdersLaw--;
                    MySingleton.pack.Array.remove(i);
                    System.out.println("delete Law");
                }
            }
        }
    }

    void Update(long time) {

        checkTime(time);

        if (time % N1 == 0) {
            double x = Math.random();
            if (x <= P1) {
                Person A = new Person(PersonImage, Math.random(),Math.random());
                int id = (int)(Math.random()*1000);

                A.timespawn = (int)time;
                A.id = (int)(Math.random()*1000);
                MySingleton.pack.numbersPerson++;
                MySingleton.pack.Array.add(A);
                MySingleton.pack.Tree.put(A.id, MySingleton.pack.Array.size()-1);
                MySingleton.pack.hash.put(A.id,time);
                System.out.println("Появился новый налогоплательщик! ");
            }
        }
        if (time % N2 == 0) {
            double x = Math.random();
            if (x <= P2) {
                law A = new law(lawImage, Math.random(), Math.random());
                int id = (int)(Math.random()*1000);

                A.timespawn = (int)time;
                A.id = (int)(Math.random()*1000);
                MySingleton.pack.numdersLaw++;
                MySingleton.pack.Array.add(A);
                MySingleton.pack.Tree.put(A.id, MySingleton.pack.Array.size()-1);
                MySingleton.pack.hash.put(A.id, time);
                System.out.println("Основана новая фирма! ");
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
