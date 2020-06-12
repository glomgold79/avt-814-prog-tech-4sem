package app;

public class DroneAI extends BaseAI {

    final Object tmp = new Object();

    @Override
    public void run() {
        while (move) {
            synchronized (tmp) {
                if (GUI.stopDroneAI) {
                    try {
                        tmp.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < Habitat.array.size(); i++) {
                    if (Habitat.array.get(i) instanceof Drone) {
                        Habitat.array.get(i).move();
                    }
                }
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
