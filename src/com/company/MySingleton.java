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
    public double timeKoeff;
    public Timer timer;
    boolean runTime, runPersonTime, runLawTime,work;

    BaseAIPerson AIPerson;
    BaseAILaw AILaw;

    public void clear(){
        Array.clear();
        numdersLaw = 0;
        numbersPerson = 0;
        Tree.clear();
        hash.clear();
        runTime = runLawTime = runPersonTime = false;
    }

    {
        Array = new Vector<>();
        Tree = new TreeMap<>();
        hash = new HashMap<>();
        timer = null;
        numbersPerson = 0;
        numdersLaw = 0;
        timeKoeff = 0.1;
        runTime = false;
        runLawTime = false;
        runPersonTime = false;
        AIPerson = null;
        AILaw = null;
        work=true;
    }
}