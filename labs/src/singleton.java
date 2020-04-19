import java.util.*;

class Singleton { // паттерн - Singleton

    private static Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null)
            instance = new Singleton();
        return instance;
    }

    private LinkedList<student> students = new LinkedList<>();
    private HashSet<Integer> studentsID = new HashSet<>(); // идентификаторы "живых" объектов
    private TreeMap<Integer, Long> timeOfDeath = new TreeMap<>(); // время смерти объектор, ключ - строка-идентиификатор из HashSet, значение - время смерти Long

    public void addStudent(student student) {
        synchronized (students) {
            students.add(student);
            studentsID.add(student.studentID);
            timeOfDeath.put(student.studentID, student.timeOfDeath);
        }
    }
    public LinkedList<student> getStudents() {
        return students;
    }
    public TreeMap<Integer, Long> getTreeMap() {
        return timeOfDeath;
    }
    public student getStudent(int studentIndex) {
        return students.get(studentIndex);
    }
    public void checkList(long timeFromBeginning, Habitat habitat) { // проверка списков для удаления
        synchronized (students) {
            ArrayList<Integer> indexesForDelete = new ArrayList<>();
            for (Map.Entry e : timeOfDeath.entrySet()) {
                if (timeFromBeginning >= (long) e.getValue()) {
                    indexesForDelete.add((int) e.getKey());
                }
            }
            for (int i = 0; i < indexesForDelete.size(); i++) { // обход массива с идентификаторами объектов для удалениия
                int key = indexesForDelete.get(i);
                habitat.numberOfStudents--;
                studentsID.remove(key);
                timeOfDeath.remove(key);

                int index = 0;
                for (int j = 0; j < students.size(); j++) {
                    student student = students.get(j);
                    if (student.studentID == key) {
                        if (student instanceof maleStudent) {
                            habitat.maleStudents--;
                        }
                        else habitat.femaleStudents--;
                        index = j;
                        break;
                    }
                }
                students.remove(index);
            }
        }
    }
    public void zero() {
        synchronized (students) {
            students.clear();
            studentsID.clear();
            timeOfDeath.clear();
        }
    }
}