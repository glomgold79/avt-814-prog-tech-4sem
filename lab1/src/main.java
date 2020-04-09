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

class Habitat {
    
    int N1 = 2, N2 = 3; // время генерации студентов и студенток соответсвенно
    double P1 = 0.3, P2 = 0.4; // вероятность генерации студентов и студенток соответсвенно

    int windowX, windowY; //размеры области экрана

    int studentsLimit = 10;
    student[] students = new student[studentsLimit]; //массив с информацией со студентами
    int numberOfStudents = 0; //количество студентов

    int maleStudents = 0, femaleStudents = 0;

    public int gcd(int a, int b) { //вычисление НОД по алгоритму Евклида
        while (a != b) {
            if (a > b) {
                a = a - b;
            } else {
                b = b - a;
            }
        }
        return a;
    }
    int period = gcd(N1*1000, N2*1000);
    long timeFromBeginning = 0; //время симуляции
    boolean infoVision = false;

    boolean simulation = false; //идёт ли симуляция
    boolean infoAfterFinishVisible = false;

    public Habitat(int x, int y) {
        windowX = x;
        windowY = y;

    }


    public void update(long timeFromBeginning, JPanel label) { //метод обновления среды, генерация новых студентов
        String s1 = "";
        s1 = s1 + timeFromBeginning;
        System.out.println(s1);
        if (numberOfStudents < studentsLimit) {
            if (timeFromBeginning % (N1*1000) == 0) { //попытка генерации студента
                double random = Math.random();
                if (0 < random & random < P1) {
                    students[numberOfStudents++] = new maleStudent((int) (Math.random() * windowX), (int) (Math.random() * windowY));
                    drawStudent(label, maleStudent.maleStudentImage, (int) students[numberOfStudents - 1].x, (int) students[numberOfStudents - 1].y);
                    maleStudents++;
                    System.out.println("Студент сгенерирован");
                }
            }
            if (timeFromBeginning % (N2*1000) == 0 & (numberOfStudents < studentsLimit)) { //попытка генерации студентки
                double random = Math.random();
                if (0 < random & random < P2) {
                    students[numberOfStudents++] = new femaleStudent((int) (Math.random() * windowX), (int) (Math.random() * windowY));
                    drawStudent(label, femaleStudent.femaleStudentImage, (int) students[numberOfStudents - 1].x, (int) students[numberOfStudents - 1].y);
                    femaleStudents++;
                    System.out.println("Студентка сгенерирована");
                }
            }
        }
        else System.out.println("Максимальное количество студентов");
    }
    public void drawStudent(JPanel label, Image img, int X, int Y) {
        Graphics g = label.getGraphics();
        g.drawImage(img, X, Y, null);
    }
    public void zero() {
        students = new student[studentsLimit];
        numberOfStudents = 0;
        maleStudents = 0;
        femaleStudents = 0;
        timeFromBeginning = 0;

    }
}


abstract class student implements IBehaviour { // класс студента
    public int x, y; // положение студентов в среде
    student(int x, int y) { //конструктор студента
        this.x = x;
        this.y = y;
    }
}

interface IBehaviour { // поведение объектов в среде
}

class maleStudent extends student { //класс студента мужского пола
    final static Image maleStudentImage = new ImageIcon("lab1\\img\\maleStudent.jpg").getImage();
    maleStudent(int x, int y) {
        super(x, y);
    }
}

class femaleStudent extends student { //класс студента женского пола
    final static Image femaleStudentImage = new ImageIcon("lab1\\img\\femaleStudent.jpg").getImage();
    femaleStudent(int x, int y) {
        super(x, y);
    }
}