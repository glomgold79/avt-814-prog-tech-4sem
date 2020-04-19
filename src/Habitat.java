

import javax.swing.*;
import java.awt.*;
import java.util.*;

class Habitat {
    static ArrayList<Bee> array; // массив объектов
    static HashSet<Integer> identifiers; // массив идентификаторов объектов
    static TreeMap<Integer, Long> birthdays; // массив времени рождения объектов
    static int numberOfDrones; // переменные для записи статистики о родившихся животных
    static int numberOfWorkers;
    static int N1 = 5;
    static int N2 = 5; // время генерации
    static int K = 50;
    static int P = 80; // вероятность генерации в %
    static int iK = 0;
    static int droneTimeOfLife = 20;
    static int WorkerTimeOfLife = 20;
    private Image droneImg = new ImageIcon("drone.png").getImage();
    private Image workerImg = new ImageIcon("worker.png").getImage();
    static int areaSizeX, areaSizeY; // размер области генерации
    public long time=0;
    Habitat(int x, int y) {
        areaSizeX = x- 250;
        areaSizeY = y- 250;
        array = new ArrayList<>();
        identifiers = new HashSet<>();
        birthdays = new TreeMap<>();
    }

    void update(long time) {
        this.time=time;
        int x, y, id;
        System.out.println("time: " + time);
        for (int i = 0; i < Habitat.array.size(); i++) {
            if (Habitat.array.get(i) instanceof Drone && (time - Habitat.array.get(i).timeOfBirth) >= droneTimeOfLife) {
                array.remove(i);
                birthdays.remove(i);
                identifiers.remove(i);
                --i;
                --iK;
                System.out.println("Drone has deleted.");
            } else if (Habitat.array.get(i) instanceof Worker && (time - Habitat.array.get(i).timeOfBirth) >= WorkerTimeOfLife) {
                array.remove(i);
                birthdays.remove(i);
                identifiers.remove(i);
                --i;
                System.out.println("Worker has deleted.");
            }
        }
        if (iK < (Habitat.array.size() * K / 100) && time % N1 == 0 && time != 0) { // вероятность выполнения условия
            x = new Random().nextInt(areaSizeX );
            y = new Random().nextInt(areaSizeY );
            id = new Random().nextInt();
            Drone drone = new Drone(droneImg, x, y, time,id);
            array.add(drone);
            identifiers.add(id);
            birthdays.put(id, drone.timeOfBirth);
            System.out.println("drone has generated. time of birth: " + drone.timeOfBirth + ", id: " + id);
            numberOfDrones++;
            iK++;
        }
        if (new Random().nextInt(100) < P && time % N2 == 0 && time != 0) { // вероятность выполнения условия
            x = new Random().nextInt(areaSizeX );
            y = new Random().nextInt(areaSizeY );
            id = new Random().nextInt();
            Worker worker = new Worker(workerImg, x, y, time,id);
            array.add(worker);
            identifiers.add(id);
            birthdays.put(id, worker.timeOfBirth);
            System.out.println("Worker has generated. time of birth: " + worker.timeOfBirth + ", id: " + id);
            numberOfWorkers++;
        }
    }
}