package com.company;

import java.util.Timer;

public class MySingleton {

    private static MySingleton Singl;

    public static Package pack;

    public static MySingleton getInitialized(){
        if(Singl == null){
            Singl = new MySingleton();
        }
        return Singl;
    }

    {
        pack = new Package();
    }

    private MySingleton(){

    }
}

class Package{

    public card_index[] Array;
    public int sizeArray;
    public int numbersPerson;
    public int numdersLaw;
    public Timer timer;

    public void add(card_index A){
        card_index[] newArray = new card_index[sizeArray+1];
        for(int i = 0; i < sizeArray; i++)
            newArray[i] = Array[i];
        newArray[sizeArray] = A;
        sizeArray++;
        Array = null;
        Array = newArray;
    }

    public void clear(){
        Array = null;
        Array = new card_index[0];
        numdersLaw = 0;
        numbersPerson = 0;
        sizeArray = 0;
    }

    {
        Array = new card_index[0];
        timer = null;
        numbersPerson = 0;
        numdersLaw = 0;
        sizeArray = 0;
    }
}