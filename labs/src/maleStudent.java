import javax.swing.*;
import java.awt.*;

class maleStudent extends student { //класс студента мужского пола
    final static Image maleStudentImage = new ImageIcon("labs\\img\\maleStudent.jpg").getImage();
    public int direction; //направление движения студента
    maleStudent(int x, int y, long timeOfBirth, long timeOfDeath, int studentID) {
        super(x, y, timeOfBirth, timeOfDeath, studentID);
        this.direction = (int) (Math.random() * 4); // генерация случайного направления
        speed = 5;

    }

    @Override
    public void changeDirection() {
        this.direction = (int) (Math.random() * 4); // генерация случайного направлени
    }
    @Override
    public void move() {
        if (direction == 0) { // вверх
            this.y -= speed;
        } else if (direction == 1) { // вниз
            this.y += speed;
        } else if (direction == 2) { // вправо
            this.x += speed;
        } else { // влево
            this.x -= speed;
        }
    }
}