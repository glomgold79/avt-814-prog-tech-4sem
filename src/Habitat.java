

import javax.swing.*;
import java.awt.*;
import java.util.*;

class Habitat {
    static ArrayList<Bee> array; // массив объектов
    static HashSet<Integer> identifiers; // массив идентификаторов объектов
    static TreeMap<Integer, Long> birthdays; // массив времени рождения объектов
    static int numberOfDrones; // переменные для записи статистики о родившихся животных
    static int numberOfWorkers;
    static int N1 = 4;
    static int N2 = 3; // время генерации
    static int K = 30;
    static int P = 80; // вероятность генерации в %
    static int iK = 0;
    static int droneTimeOfLife = 8;
    static int WorkerTimeOfLife = 6;
    private Image droneImg = new ImageIcon("drone.png").getImage();
    private Image workerImg = new ImageIcon("worker.png").getImage();
    private int areaSizeX, areaSizeY; // размер области генерации

    Habitat(int x, int y) {
        areaSizeX = x;
        areaSizeY = y;
        array = new ArrayList<>();
        identifiers = new HashSet<>();
        birthdays = new TreeMap<>();
    }

    void update(long time) {
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
            x = new Random().nextInt(areaSizeX - 175);
            y = new Random().nextInt(areaSizeY - 175);
            id = new Random().nextInt();
            Drone drone = new Drone(droneImg, x, y, id, time);
            array.add(drone);
            identifiers.add(id);
            birthdays.put(id, drone.timeOfBirth);
            System.out.println("drone has generated. time of birth: " + drone.timeOfBirth + ", id: " + id);
            numberOfDrones++;
            iK++;
        }
        if (new Random().nextInt(100) < P && time % N2 == 0 && time != 0) { // вероятность выполнения условия
            x = new Random().nextInt(areaSizeX - 150);
            y = new Random().nextInt(areaSizeY - 150);
            id = new Random().nextInt();
            Worker puppy = new Worker(workerImg, x, y, id, time);
            array.add(puppy);
            identifiers.add(id);
            birthdays.put(id, puppy.timeOfBirth);
            System.out.println("Worker has generated. time of birth: " + puppy.timeOfBirth + ", id: " + id);
            numberOfWorkers++;
        }
    }
}