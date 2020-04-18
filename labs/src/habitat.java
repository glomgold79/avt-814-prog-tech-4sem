import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

class Habitat { //класс среды с хранящимися данными

    final java.util.Timer[] timer = {new java.util.Timer()};
    int N1, N2; // время генерации студентов и студенток соответсвенно
    double P1, P2; // вероятность генерации студентов и студенток соответсвенно

    int windowX, windowY; //размеры области экрана

    Singleton singleton =  Singleton.getInstance();

    int numberOfStudents = 0; //количество студентов
    int maleStudents = 0, femaleStudents = 0;

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

    public Habitat(int x, int y) {
        windowX = x;
        windowY = y;
    }

    public void update(long timeFromBeginning, JPanel classRoom) { //метод обновления среды, генерация новых студентов
        String s1 = "";
        s1 = s1 + timeFromBeginning;
        System.out.println(s1);

        if (timeFromBeginning % (N1*1000) == 0) { //попытка генерации студента
            double random = Math.random();
            if (0 < random & random < P1) {
                singleton.addStudent(new maleStudent((int) (Math.random() * windowX), (int) (Math.random() * windowY)));
                student student = singleton.getStudent(numberOfStudents);
                drawStudent(classRoom, maleStudent.maleStudentImage, student.x, student.y);
                maleStudents++;
                numberOfStudents++;
                System.out.println("Студент сгенерирован");
            }
        }

        if (timeFromBeginning % (N2*1000) == 0) { //попытка генерации студентки
            double random = Math.random();
            if (0 < random & random < P2) {
                singleton.addStudent(new femaleStudent((int) (Math.random() * windowX), (int) (Math.random() * windowY)));
                student student = singleton.getStudent(numberOfStudents);
                drawStudent(classRoom, femaleStudent.femaleStudentImage, student.x, student.y);
                femaleStudents++;
                numberOfStudents++;
                System.out.println("Студентка сгенерирована");
            }
        }


    }
    public void drawStudent(JPanel classRoom, Image img, int X, int Y) {
        Graphics g = classRoom.getGraphics();
        g.drawImage(img, X, Y, null);
    }
    public void startSimulation(JLabel timeInfo, JPanel classRoom, GUI.controlPanel.birthPeriodPanel birthPeriodPanel, GUI.controlPanel.comboBoxPanel comboBoxPanel) {

        if (!simulation) {
            N1 = birthPeriodPanel.getMalePeriod();
            N2 = birthPeriodPanel.getFemalePeriod();
            P1 = comboBoxPanel.maleChance();
            P2 = comboBoxPanel.femaleChance();
            period = gcd(N1*1000, N2*1000);

            simulation = true;
            System.out.println("Нажата клавиша B или нажата кнопка 'Старт', симуляция началась");
            //начало симуляции

            timer[0].schedule(new TimerTask() {
                @Override
                public void run() {
                    timeFromBeginning += period;
                    update(timeFromBeginning, classRoom);
                    timeInfo.setText("Время симуляции: " + timeFromBeginning/1000 + "с");
                }
            }, period, period);
        }
        else System.out.println("Нельзя начать симуляцию - симуляция идёт.");
    }
    public void stopSimulation(JLabel timeInfo, GUI.dialogWindow dialog, GUI.classRoom classRoom, GUI.dialogWindow dialogWindow) {

        if (simulation) {

            int simulationTime = (int) timeFromBeginning/1000;
            timer[0].purge();
            timer[0].cancel();
            timer[0] = new Timer();
            timeInfo.setText("Симуляция остановлена.");
            dialogWindow.textArea.setText("Время симуляции:"  + simulationTime + "с\nКоличество студентов: " + maleStudents + "\nКоличество студенток: " + femaleStudents);
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
        if (infoVision) {
            infoVision = false;
            showSimulationTimePanel.setVisible(infoVision);
        }
        else  {
            infoVision = true;
            showSimulationTimePanel.setVisible(infoVision);
        }
    }
    public void zero() {
        singleton.zero();
        numberOfStudents = 0;
        maleStudents = 0;
        femaleStudents = 0;
        timeFromBeginning = 0;

    }
}