import javax.swing.*;
import java.awt.*;

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