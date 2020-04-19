import javax.swing.*;
import java.awt.*;

class femaleStudent extends student { //класс студента женского пола
    double angle; // угол в радианах
    double speed; // скорость в радианах
    int R = 3; // радиус
    final static Image femaleStudentImage = new ImageIcon("labs\\img\\femaleStudent.jpg").getImage();
    femaleStudent(int x, int y, long timeOfBirth, long timeOfDeath, int studentID) {
        super(x, y, timeOfBirth, timeOfDeath, studentID);
        this.angle = Math.random() * 2 * Math.PI;
        this.speed = Math.PI / 200;
    }

    @Override
    public void move() {
        this.x += (int) (Math.cos(angle) * R);
        this.y += (int) (Math.sin(angle) * R);
        angle += speed;
    }
}