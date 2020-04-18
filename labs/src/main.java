import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.border.Border;

public class main {

    public static void main(String[] args) {

        int windowX = 1000, windowY = 600; //размеры окна программы
        JFrame myWindow = new JFrame("Class");
        myWindow.setLayout(new BorderLayout());
        myWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myWindow.setSize(windowX, windowY);
        myWindow.setResizable(false);

        JPanel classRoom = new JPanel();
        JPanel infoAboutSimulation = new JPanel();

        infoAboutSimulation.setPreferredSize(new Dimension(200, 0));

        classRoom.setBackground(Color.WHITE);
        infoAboutSimulation.setBackground(Color.WHITE);

        classRoom.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        infoAboutSimulation.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        infoAboutSimulation.setLayout(new BorderLayout());
        Font font = new Font("Verdana", Font.ROMAN_BASELINE, 12);
        JLabel info = new JLabel("Время симуляции: ");
        info.setFont(font);
        info.setVisible(false);
        infoAboutSimulation.add(info, BorderLayout.CENTER);
        infoAboutSimulation.setBackground(Color.RED);

        classRoom.setLayout(new BorderLayout());
        JLabel infoAfterFinish = new JLabel(); //информация после окончания симуляции
        infoAfterFinish.setVerticalTextPosition(JLabel.CENTER);
        infoAfterFinish.setVisible(false);
        classRoom.add(infoAfterFinish, BorderLayout.CENTER);



        myWindow.add(classRoom, BorderLayout.CENTER);
        myWindow.add(infoAboutSimulation, BorderLayout.EAST);

        myWindow.setVisible(true);
        final Timer[] timer = {new Timer()};
        Habitat habitat = new Habitat(classRoom.getWidth(), classRoom.getHeight());
        myWindow.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case 66: //клавиша B - начало симуляции

                        if (!habitat.simulation) {
                            habitat.simulation = true;
                            System.out.println("Нажата клавиша B, симуляция началась");
                            //начало симуляции
                            if (habitat.infoAfterFinishVisible) {
                                habitat.infoAfterFinishVisible = false;
                                infoAfterFinish.setVisible(habitat.infoAfterFinishVisible);
                            }
                            timer[0].schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    habitat.timeFromBeginning += habitat.period;
                                    habitat.update(habitat.timeFromBeginning, classRoom);
                                    info.setText("Время симуляции: " + habitat.timeFromBeginning/1000 + "с");
                                }
                            }, habitat.period, habitat.period);
                        }
                        else System.out.println("Нельзя начать симуляцию - симуляция идёт.");
                        break;

                    case 69: //клавиша  E - конец симуляции
                        if (habitat.simulation) {
                            habitat.simulation = false;
                            int sumulationTime = (int) habitat.timeFromBeginning/1000;
                            timer[0].purge();
                            timer[0].cancel();
                            timer[0] = new Timer();
                            info.setText("Симуляция остановлена.");

                            if (!habitat.infoAfterFinishVisible) {
                                habitat.infoAfterFinishVisible = true;
                                infoAfterFinish.setVisible(habitat.infoAfterFinishVisible);
                                infoAfterFinish.setText("<html> Время последней симуляции: " + sumulationTime + "с<br> Количество студентов: " + habitat.maleStudents + "</br>" + "<br>Количество студенток: " + habitat.femaleStudents + "</br></html>");
                            }

                        }
                        else System.out.println("Нельзя закончить симуляцию - симуляция не начата.");
                        habitat.zero();
                        System.out.println("Нажата клавиша E, симуляция закончена");

                        break;
                    case 84: //клавиша T - показывание информации
                        System.out.println("Нажата клавиша T, показать/скрыть информацию о симуляции");
                        if (habitat.infoVision) {
                            habitat.infoVision = false;
                            info.setVisible(habitat.infoVision);
                            infoAboutSimulation.setBackground(Color.RED);
                        }
                        else  {
                            habitat.infoVision = true;
                            info.setVisible(habitat.infoVision);
                            infoAboutSimulation.setBackground(Color.GREEN);
                        }

                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
    }
}

