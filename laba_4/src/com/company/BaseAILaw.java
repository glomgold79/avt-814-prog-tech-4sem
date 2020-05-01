package com.company;


public class BaseAILaw extends BaseAI {

    @Override
    public synchronized void run(){
        for(;;) {
            if (MySingleton.pack.runLawTime == false)
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            else {
                try {
                    this.sleep((long) (1000 * MySingleton.pack.timeKoeff));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < MySingleton.pack.Array.size(); i++) {
                    if (MySingleton.pack.Array.get(i).getClass() == new law().getClass()) {
                        MySingleton.pack.Array.get(i).Move();
                    }
                }
            }
        }
    }
}
