package app;

import javax.swing.*;

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
    static int N=2;
    static int droneTimeOfLife = 20;
    static int WorkerTimeOfLife = 20;
    private ImageIcon droneImg = new ImageIcon("drone.png");
    private ImageIcon workerImg = new ImageIcon("worker.png");
    static int areaSizeX, areaSizeY; // размер области генерации

    Habitat(int x, int y) {
        areaSizeX = x - 250;
        areaSizeY = y - 250;
        array = new ArrayList<>();
        identifiers = new HashSet<>();
        birthdays = new TreeMap<>();
    }

    void update(long time) {
        int x, y, id;
        //System.out.println("time: " + time);
        for (int i = 0; i < Habitat.array.size(); i++) {
            if (Habitat.array.get(i) instanceof Drone && (time - Habitat.array.get(i).timeOfBirth) >= droneTimeOfLife) {
                array.remove(i);
                birthdays.remove(i);
                identifiers.remove(i);
                --i;
                --iK;
                --numberOfDrones;
                //System.out.println("app.Drone has deleted.");
            } else if (Habitat.array.get(i) instanceof Worker && (time - Habitat.array.get(i).timeOfBirth) >= WorkerTimeOfLife) {
                array.remove(i);
                birthdays.remove(i);
                identifiers.remove(i);
                --i;
                --numberOfWorkers;
                //System.out.println("app.Worker has deleted.");
            }
        }
        if (iK < (Habitat.array.size() * K / 100) && time % N1 == 0 && time != 0) { // вероятность выполнения условия
            x = new Random().nextInt(areaSizeX);
            y = new Random().nextInt(areaSizeY);
            Drone drone = new Drone(droneImg, x, y, time);
            boolean isNumberExists = false;
            while (!isNumberExists) {
                id = new Random().nextInt();
                isNumberExists = identifiers.add(id);
                if (isNumberExists) drone.setID(id);
                array.add(drone);
                birthdays.put(id, drone.timeOfBirth);
                //System.out.println("app.Drone has generated. time of birth: " + drone.timeOfBirth + ", id: " + id +
                //      "\nx: " + x + " y: " + y);
            }
            iK++;
            numberOfDrones++;
        }
        if (new Random().nextInt(100) < P && time % N2 == 0 && time != 0) { // вероятность выполнения условия
            x = new Random().nextInt(areaSizeX);
            y = new Random().nextInt(areaSizeY);
            Worker worker = new Worker(workerImg, x, y, time);
            boolean isNumberExists = false;
            while (!isNumberExists) {
                id = new Random().nextInt();
                isNumberExists = identifiers.add(id);
                if (isNumberExists) worker.setID(id);
                array.add(worker);
                birthdays.put(id, worker.timeOfBirth);
                //System.out.println("app.Worker has generated. time of birth: " + worker.timeOfBirth + ", id: " + id);
            }
            numberOfWorkers++;
        }
    }
    static ArrayList<Bee> getBee(){
        ArrayList<Bee> bee = new ArrayList<>();

        for (int i = 0; i <N &&i<array.size(); i++) {

            bee.add(array.get(0));
            if (array.get(0) instanceof Drone) {
                numberOfDrones--;
                iK--;
            }
            else numberOfWorkers--;

            array.remove(0);
        }
        return bee;
    }


}
