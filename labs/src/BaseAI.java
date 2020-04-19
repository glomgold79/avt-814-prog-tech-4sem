import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

abstract class BaseAI extends Thread {

    Singleton singleton =  Singleton.getInstance();
    LinkedList<student> linkedList = singleton.getStudents();


    public boolean simulation;
    public void run() {

        while (true) {
            try {
                this.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            move();
        }

    }
    void move() {} // метод движения который будет переопределен

    public void threadWait() {
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}