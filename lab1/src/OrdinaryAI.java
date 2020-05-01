import java.util.Timer;
import java.util.TimerTask;

public class OrdinaryAI extends BaseAI{

    private int N = 300, cumN = 0;
    private int period = 5;

    public boolean isAlive = false;

    Timer timer = new Timer();

    @Override
    public void run() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (Singleton.getVector()) {
                    if (isAlive) {
                        for (Rabbit rabbit : Singleton.getVector()) {
                            if (rabbit instanceof Ordinary) {
                                if (cumN % N == 0) {
                                    ((Ordinary) rabbit).targetX = (int) (Math.random() * Habitat.gui.jPanelImage.getWidth());
                                    ((Ordinary) rabbit).targetY = (int) (Math.random() * Habitat.gui.jPanelImage.getHeight());
                                    cumN = 0;
                                }
                                int dx = (((Ordinary) rabbit).targetX - rabbit.getX());
                                int dy = (((Ordinary) rabbit).targetY - rabbit.getY());
                                int length = (int) (Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
                                int time = length / (Speed * 2); //за такое кол-во секунд будет достигнута цель

                                rabbit.setX(rabbit.getX() + (time == 0 ? 0 : dx / time));
                                rabbit.setY(rabbit.getY() + (time == 0 ? 0 : dy / time));
                            }
                        }
                        cumN += period;
                    }
                }
            }
        },0,period);
    }
}
