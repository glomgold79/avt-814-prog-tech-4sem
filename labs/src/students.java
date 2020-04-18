import javax.swing.*;
import java.awt.*;

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