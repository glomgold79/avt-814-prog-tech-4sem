import java.util.ArrayList;

class Singleton { // паттерн - Singleton

    private static Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null)
            instance = new Singleton();
        return instance;
    }

    private ArrayList<student> students = new ArrayList<>(); // массив со студентами

    public void addStudent(student student) {
        students.add(student);
    }
    public student getStudent(int studentIndex) {
        return students.get(studentIndex);
    }
    public void zero() {
        students.clear();
    }
}