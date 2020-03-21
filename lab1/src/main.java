import javax.swing.JFrame;

public class main {

    final static Habitat h = new Habitat();

    public static void main(String[] args) {
        System.out.println("РОМАН СУКСИН ПРИВЕТ");

    }
}

class Habitat extends JFrame{ //среда выполенния


    static int N1 = 1, N2 = 3; // время генерации студентов и студенток соответсвенно
    static double P1 = 0.2, P2 = 0.4; // вероятность генерации студентов и студенток соответсвенно

    static int windowX = 600, windowY = 600; //размеры области экрана

    student[] array = new student[50]; //массив с информацией со студентами

    static int numberOfStudents = 0; //количество студентов

    public Habitat() { //конструктор среды
        super("Никита Носов");
        setBounds(windowX, windowY, windowX, windowY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}


interface IBehaviour { // поведение объектов в среде

}

abstract class student implements IBehaviour { // класс студента
    public double x, y; // положение студентов в среде

    student() { //конструктор студента
    }
}

class maleStudent extends student { //класс студента мужского пола
    maleStudent() { //конструктор студента мужского пола
    }
}

class femaleStudent extends student { //класс студента женского пола
    femaleStudent() { // констркутор студента женского пола
    }
}