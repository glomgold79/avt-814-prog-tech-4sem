package com.company;

import java.util.HashMap;
import java.util.Timer;
import java.util.TreeMap;
import java.util.Vector;

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

    public Vector<card_index> Array;
    public TreeMap<Integer,Integer> Tree;
    public HashMap<Integer,Long> hash;
    public int numbersPerson;
    public int numdersLaw;
    public Timer timer;

    public void clear(){
        Array.clear();
        numdersLaw = 0;
        numbersPerson = 0;
        Tree.clear();
        hash.clear();
    }

    {
        Array = new Vector<>();
        Tree = new TreeMap<>();
        hash = new HashMap<>();
        timer = null;
        numbersPerson = 0;
        numdersLaw = 0;
    }
}