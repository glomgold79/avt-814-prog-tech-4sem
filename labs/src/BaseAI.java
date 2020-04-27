import java.util.LinkedList;

abstract class BaseAI extends Thread {

    Singleton singleton =  Singleton.getInstance();
    LinkedList<student> linkedList = singleton.getStudents();

    public boolean simulation;
    public void run() {
        while (true) {
            if (!simulation) {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                this.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            move();
        }

    }
    public void threadWait(){
        this.simulation = false;
    }
    public void startSimulation(int priority){
        simulation = true;
        this.setPriority(priority);
        this.start();
    }
    public void rerun(int priority) {
        simulation = true;
        this.setPriority(priority);
        synchronized (this) {
            try {
                this.notify();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    void move() {} // метод движения который будет переопределен


}