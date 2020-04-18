import javax.swing.*;
import java.awt.*;

abstract class student implements IBehaviour { // класс студента
    public int x, y; // положение студентов в среде
    public long timeOfBirth, timeOfDeath;
    public int studentID; // уникальный ID студента

    student(int x, int y, long timeOfBirth, long timeOfDeath, int studentID) { //конструктор студента
        this.x = x;
        this.y = y;
        this.timeOfBirth = timeOfBirth;
        this.timeOfDeath = timeOfDeath;
        this.studentID = studentID;
    }
}

interface IBehaviour { // поведение объектов в среде
}

class maleStudent extends student { //класс студента мужского пола
    final static Image maleStudentImage = new ImageIcon("labs\\img\\maleStudent.jpg").getImage();
    maleStudent(int x, int y, long timeOfBirth, long timeOfDeath, int studentID) {
        super(x, y, timeOfBirth, timeOfDeath, studentID);
    }
}

class femaleStudent extends student { //класс студента женского пола
    final static Image femaleStudentImage = new ImageIcon("labs\\img\\femaleStudent.jpg").getImage();
    femaleStudent(int x, int y, long timeOfBirth, long timeOfDeath, int studentID) {
        super(x, y, timeOfBirth, timeOfDeath, studentID);
    }
}