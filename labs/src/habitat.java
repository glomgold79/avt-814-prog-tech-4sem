import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


class Habitat { //класс среды с хранящимися данными

    final Timer[] timer = {new Timer()};
    int N1, N2; // время генерации студентов и студенток соответсвенно
    int lifeTimeMale, lifeTimeFemale; // время жизни студентов и студенток соответственно
    double P1, P2; // вероятность генерации студентов и студенток соответсвенно

    int windowX, windowY; //размеры области экрана

    Singleton singleton =  Singleton.getInstance();

    int numberOfStudents = 0; //количество студентов
    int maleStudents = 0, femaleStudents = 0;
    int studentID = 0; // id следующего студента

    private int gcd(int a, int b) { //вычисление НОД по алгоритму Евклида
        while (a != b) {
            if (a > b) {
                a = a - b;
            } else {
                b = b - a;
            }
        }
        return a;
    }

    int period;
    long timeFromBeginning = 0; //время симуляции

    boolean infoVision = false; // показывается ли информация о времени симуляции
    boolean simulation = false; //идёт ли симуляция
    boolean infoAfterFinishVisible = false; //будет ли отображено диалоговое окно после остановки симуляции
    boolean threadsOnPause = false; // нахожятся ли потоки в режиме сна (если остановить симуляцию и потом запустить еще раз)

    maleStudentsAI maleAI; // поток поведения студентов
    //femaleStudentsAI femaleAI; // поток поведения студенток


    public Habitat(int x, int y) {
        windowX = x;
        windowY = y;

        maleAI = new maleStudentsAI();
        //femaleAI = new femaleStudentsAI();
    }

    public void update(long timeFromBeginning, JPanel classRoom) { //метод обновления среды, генерация новых студентов
        String s1 = "";
        s1 = s1 + timeFromBeginning;
        System.out.println(s1);
        if (timeFromBeginning % (N1*1000) == 0) { //попытка генерации студента
            double random = Math.random();
            if (0 < random & random < P1) {
                singleton.addStudent(new maleStudent((int) (Math.random() * windowX), (int) (Math.random() * windowY), timeFromBeginning, timeFromBeginning + lifeTimeMale * 1000, studentID++));
                //student student = singleton.getStudent(numberOfStudents);
                maleStudents++;
                numberOfStudents++;
                System.out.println("Студент сгенерирован");
            }
        }
        if (timeFromBeginning % (N2*1000) == 0) { //попытка генерации студентки
            double random = Math.random();
            if (0 < random & random < P2) {
                singleton.addStudent(new femaleStudent((int) (Math.random() * windowX), (int) (Math.random() * windowY), timeFromBeginning, timeFromBeginning + lifeTimeFemale * 1000, studentID++));
                student student = singleton.getStudent(numberOfStudents);
                femaleStudents++;
                numberOfStudents++;
                System.out.println("Студентка сгенерирована");
            }
        }
        singleton.checkList(timeFromBeginning, this);
        drawAll(classRoom);

    }

    public void drawAll(JPanel classRoom) {

        Graphics g = classRoom.getGraphics();
        g.clearRect(0,0, classRoom.getWidth(), classRoom.getHeight());
        LinkedList<student> myClass = singleton.getStudents();
        synchronized (myClass) {
            for (int i = 0; i < myClass.size(); i++) {
                student student = myClass.get(i);
                if (student instanceof maleStudent) {
                    g.drawImage(maleStudent.maleStudentImage, student.getX(), student.getY(), null);
                } else g.drawImage(femaleStudent.femaleStudentImage, student.getX(), student.getY(), null);
            }
        }
    }
    public void startSimulation(GUI.controlPanel controlPanel, JPanel classRoom) {

        if (!simulation) {
            N1 = controlPanel.birthPeriodPanel.getMalePeriod();
            N2 = controlPanel.birthPeriodPanel.getFemalePeriod();
            lifeTimeMale = controlPanel.lifeTimePanel.getMaleLifeTime();
            lifeTimeFemale = controlPanel.lifeTimePanel.getFemaleLifeTime();
            P1 = controlPanel.comboBoxPanel.maleChance();
            P2 = controlPanel.comboBoxPanel.femaleChance();
            period = 50;

            simulation = true;
            System.out.println("Нажата клавиша B или нажата кнопка 'Старт', симуляция началась");
            //начало симуляции

            timer[0].schedule(new TimerTask() {
                @Override
                public void run() {
                    timeFromBeginning += 50;
                    update(timeFromBeginning, classRoom);
                    controlPanel.showSimulationTimePanel.timeInfo.setText("Время симуляции: " + timeFromBeginning/1000 + "с");
                }
            }, 50, 50);



            maleAI.start();
            if (maleAI.isAlive()) {
                System.out.println("Поток работает и запущен");
            }


        }
        else System.out.println("Нельзя начать симуляцию - симуляция идёт.");
    }
    public void stopSimulation(JLabel timeInfo, GUI.dialogWindow dialog, GUI.classRoom classRoom) {

        if (simulation) {

            int simulationTime = (int) timeFromBeginning/1000;
            timer[0].purge();
            timer[0].cancel();
            timer[0] = new Timer();

            maleAI.threadWait();


            timeInfo.setText("Симуляция остановлена.");
            dialog.textArea.setText("Время симуляции:"  + simulationTime + "с\nКоличество студентов: " + maleStudents + "\nКоличество студенток: " + femaleStudents);

            if (infoAfterFinishVisible) {

                dialog.setVisible(infoAfterFinishVisible);
            }
            else {
                zero();
                classRoom.repaint();
                simulation = false;
            }


        }
        else System.out.println("Нельзя закончить симуляцию - симуляция не начата.");

    }
    public void showTimeSimulation(JPanel showSimulationTimePanel) {

        System.out.println("Нажата клавиша T или выбран checkBox, показать/скрыть информацию о симуляции");
        infoVision = !infoVision;
        showSimulationTimePanel.setVisible(infoVision);
    }
    public void zero() {
        singleton.zero();
        numberOfStudents = 0;
        maleStudents = 0;
        femaleStudents = 0;
        timeFromBeginning = 0;
        studentID = 0;

    }
}