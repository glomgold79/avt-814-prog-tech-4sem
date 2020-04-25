import java.util.Timer;
import java.util.TimerTask;

public class AlbinoAI extends BaseAI{

    boolean isActive = false;

    Timer timer = new Timer();

    @Override
    public void run() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (Singleton.getVector()) {
                    if (isActive) {
                        for (Rabbit rabbit : Singleton.getVector()) {
                            if (rabbit instanceof Albino) {
                                if (((Albino) rabbit).DirectionIsToRight) {
                                    if (rabbit.getX() + Speed < Habitat.gui.jPanelImage.getWidth()) {
                                        int newX = rabbit.getX() + Speed;
                                        rabbit.setX(newX);
                                    } else {
                                        ((Albino) rabbit).DirectionIsToRight = false;
                                    }
                                } else {
                                    if (rabbit.getX() - Speed > 0) {
                                        int newX = rabbit.getX() - Speed;
                                        rabbit.setX(newX);
                                    } else {
                                        ((Albino) rabbit).DirectionIsToRight = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },0,5);
    }
}
