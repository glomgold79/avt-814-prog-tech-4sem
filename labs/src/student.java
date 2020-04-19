abstract class student implements IBehaviour { // класс студента
    public int x, y; // положение студентов в среде
    public long timeOfBirth, timeOfDeath;
    public int studentID; // уникальный ID студента
    public int speed; // скорость студента

    public void move(){};
    public void setX(int x) {
        this.x = x;
    };
    public void setY(int y) {
        this.y = y;
    };
    public int getX() {
        return x;
    };
    public int getY() {
        return y;
    };
    public void changeDirection() {};

    student(int x, int y, long timeOfBirth, long timeOfDeath, int studentID) { //конструктор студента
        this.x = x;
        this.y = y;
        this.timeOfBirth = timeOfBirth;
        this.timeOfDeath = timeOfDeath;
        this.studentID = studentID;
        this.speed = 1;
    }
}