import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.*;
import java.util.Timer;

import static javax.swing.BoxLayout.Y_AXIS;

public class main {

    public static void main(String[] args) {

        GUI gui = new GUI();

    }
}


class GUI extends JFrame { //класс основного окна программы

    int windowX = 1000, windowY = 800; // размеры окна программы
    Habitat habitat; // среда выполнения

    public class classRoom extends JPanel { //класс области, в которой рисуются студенты

        classRoom() {
            super();
        }

    }
    public class controlPanel extends JPanel { // панель управления

        class startStopButtonsPanel extends JPanel { // класс панели с кнопками старт/стоп

            public JButton startButton, stopButton;
            startStopButtonsPanel() {

                super();
                this.setLayout(new BoxLayout(this, Y_AXIS));

                JPanel textPanel = new JPanel();
                JLabel text = new JLabel("Управление симуляцией:");
                textPanel.add(text);

                JPanel buttons = new JPanel(new FlowLayout()); //панель в которой 2 кнопки
                startButton = new JButton("Старт");
                stopButton = new JButton("Стоп");
                startButton.setRequestFocusEnabled(false);
                stopButton.setRequestFocusEnabled(false);
                stopButton.setEnabled(false);
                buttons.add(startButton);
                buttons.add(stopButton);

                this.add(textPanel);
                this.add(buttons);


            }
        }
        class showDialogPanel extends JPanel { // панель с 2 радиокнопками, показывать ли диалоговое окно после конца симуляции

            public JRadioButton showDialogTrue, showDialogFalse;

            showDialogPanel() {

                super();
                this.setLayout(new BoxLayout(this, Y_AXIS));

                JPanel textPanel = new JPanel();
                JLabel text = new JLabel("Показывать диалоговое окно?");
                textPanel.add(text);

                JPanel radioButtonsPanel = new JPanel(new FlowLayout());
                ButtonGroup buttonGroup = new ButtonGroup();
                showDialogTrue = new JRadioButton("Да", false);
                showDialogFalse = new JRadioButton("Нет", true);
                showDialogTrue.setRequestFocusEnabled(false);
                showDialogFalse.setRequestFocusEnabled(false);
                buttonGroup.add(showDialogTrue);
                buttonGroup.add(showDialogFalse);

                radioButtonsPanel.add(showDialogTrue);
                radioButtonsPanel.add(showDialogFalse);

               this.add(textPanel);
               this.add(radioButtonsPanel);

            }
        }
        class showSimulationTimeInfoPanel extends JPanel {

            JCheckBox showSimulationTimeCheckBox;
            showSimulationTimeInfoPanel() { // панель с 1 чекбоксом для показа время симуляции

                super();
                showSimulationTimeCheckBox = new JCheckBox("Показывать время симуляции:");
                showSimulationTimeCheckBox.setHorizontalTextPosition(JCheckBox.LEFT);
                showSimulationTimeCheckBox.setRequestFocusEnabled(false);
                this.add(showSimulationTimeCheckBox);
            }
        }
        class showSimulationTimePanel extends JPanel {

            public JLabel timeInfo;
            showSimulationTimePanel() { // панель с таймером
                super();
                timeInfo = new JLabel("Симуляция не идёт.");
                this.add(timeInfo);
            }
        }
        public class birthPeriodPanel extends JPanel { // панель с текстовыми полями для задания периода рождения объектов

            public JTextField maleStudentsSetPeriod;
            public JTextField femaleStudentsSetPeriod;
            birthPeriodPanel() {

                //панель с текстовыми полями для указания периода рождения объектов
                super();
                this.setLayout(new FlowLayout());

                JPanel maleStudentsPeriod = new JPanel();
                maleStudentsSetPeriod = new JTextField(2);
                JLabel textPeriodMaleStudent = new JLabel("Период рождения студентов: ");
                maleStudentsPeriod.add(textPeriodMaleStudent);
                maleStudentsPeriod.add(maleStudentsSetPeriod);

                JPanel femaleStudentsPeriod = new JPanel();
                femaleStudentsSetPeriod = new JTextField(2);
                JLabel textPeriodFemaleStudent = new JLabel("Период рождения студенток: ");
                femaleStudentsPeriod.add(textPeriodFemaleStudent);
                femaleStudentsPeriod.add(femaleStudentsSetPeriod);

                this.add(maleStudentsPeriod);
                this.add(femaleStudentsPeriod);

            }

            public int getMalePeriod() {
                String maleText = maleStudentsSetPeriod.getText();
                int period;
                try {
                    period = Integer.parseInt(maleText);
                }
                catch (Exception E) {
                    maleStudentsSetPeriod.setText("2");
                    this.grabFocus();
                    period = 2;
                }
                return period;
            }
            public int getFemalePeriod() {
                String femaleText = femaleStudentsSetPeriod.getText();
                int period;
                try {
                    period = Integer.parseInt(femaleText);
                }
                catch (Exception E) {
                    femaleStudentsSetPeriod.setText("3");
                    period = 3;
                }
                finally {
                    focusMainWindow();
                }
                return period;
            }

        }
        class lifeTimePanel extends JPanel {  // панель с тектовыми полями для указания времени жизни объектов

            public JTextField maleStudentsSetLifeTime;
            public JTextField femaleStudentsSetLifeTime;
            lifeTimePanel() {

                //панель с текстовыми полями для указания периода рождения объектов
                super();
                this.setLayout(new FlowLayout());

                JPanel maleStudentsLifeTime = new JPanel();
                maleStudentsSetLifeTime = new JTextField(2);
                JLabel textLifeTimedMaleStudent = new JLabel("Время жизни студентов: ");
                maleStudentsLifeTime.add(textLifeTimedMaleStudent);
                maleStudentsLifeTime.add(maleStudentsSetLifeTime);

                JPanel femaleStudentsLifeTime = new JPanel();
                femaleStudentsSetLifeTime = new JTextField(2);
                JLabel textLifeTimeFemaleStudent = new JLabel("Время жизни студенток: ");
                femaleStudentsLifeTime.add(textLifeTimeFemaleStudent);
                femaleStudentsLifeTime.add(femaleStudentsSetLifeTime);

                this.add(maleStudentsLifeTime);
                this.add(femaleStudentsLifeTime);

            }

            public int getMaleLifeTime() {
                String maleText = maleStudentsSetLifeTime.getText();
                int lifeTime;
                try {
                    lifeTime = Integer.parseInt(maleText);
                }
                catch (Exception E) {
                    maleStudentsSetLifeTime.setText("5");
                    this.grabFocus();
                    lifeTime = 5;
                }
                return lifeTime;
            }
            public int getFemaleLifeTime() {
                String femaleText = femaleStudentsSetLifeTime.getText();
                int lifeTime;
                try {
                    lifeTime = Integer.parseInt(femaleText);
                }
                catch (Exception E) {
                    femaleStudentsSetLifeTime.setText("4");
                    this.grabFocus();
                    lifeTime = 4;
                }
                return lifeTime;
            }
        }
        class comboBoxPanel extends JPanel {

            public JComboBox maleComboBox;
            public  JComboBox femaleComboBox;
            comboBoxPanel() { // панель с двумя выпадающими списками (2 комбобокса)
                super();
                this.setLayout(new FlowLayout());

                JPanel malePanel = new JPanel();
                String[] maleElements = { "0", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1"};
                maleComboBox = new JComboBox(maleElements);
                maleComboBox.setSelectedIndex(5);
                maleComboBox.setRequestFocusEnabled(false);
                JLabel textMaleComboBox = new JLabel("Вероятность генерации студента:");
                malePanel.add(textMaleComboBox);
                malePanel.add(maleComboBox);

                JPanel femalePanel = new JPanel();
                String[] femaleElements = { "0", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1"};
                femaleComboBox = new JComboBox(femaleElements);
                femaleComboBox.setSelectedIndex(5);
                femaleComboBox.setRequestFocusEnabled(false);
                JLabel textFemaleComboBox = new JLabel("Вероятность генерации студентки:");
                femalePanel.add(textFemaleComboBox);
                femalePanel.add(femaleComboBox);

                this.add(malePanel);
                this.add(femalePanel);

            }
            public double maleChance () {
                double chance = 0.1;
                chance = maleComboBox.getSelectedIndex() * 0.1;
                return chance;
            }
            public double femaleChance() {
                double chance = 0.1;
                chance = femaleComboBox.getSelectedIndex() * 0.1;
                return chance;
            }
        }
        class showAliveStudentsPanel extends JPanel {

            public JButton button;
            showAliveStudentsPanel() {
                super();

                button = new JButton("Показать живых");
                button.setRequestFocusEnabled(false);

                this.add(button);
            }
        }


        public startStopButtonsPanel startStopButtonsPanel;
        public showDialogPanel showDialogPanel;
        public showSimulationTimeInfoPanel showSimulationTimeInfoPanel;
        public showSimulationTimePanel showSimulationTimePanel;
        public birthPeriodPanel birthPeriodPanel;
        public lifeTimePanel lifeTimePanel;
        public comboBoxPanel comboBoxPanel;
        public showAliveStudentsPanel showAliveStudentsPanel;

        controlPanel() {
            super();

            this.setLayout(new BoxLayout(this, Y_AXIS));
            this.setPreferredSize(new Dimension(250, windowY));

            startStopButtonsPanel = new startStopButtonsPanel();
            showDialogPanel = new showDialogPanel();
            showSimulationTimeInfoPanel = new showSimulationTimeInfoPanel();
            birthPeriodPanel = new birthPeriodPanel();
            lifeTimePanel = new lifeTimePanel();
            comboBoxPanel = new comboBoxPanel();
            showAliveStudentsPanel = new showAliveStudentsPanel();

            showSimulationTimePanel = new showSimulationTimePanel();
            showSimulationTimePanel.setVisible(false);

            this.add(showDialogPanel);
            this.add(showSimulationTimeInfoPanel);
            this.add(showSimulationTimePanel);
            this.add(birthPeriodPanel);
            this.add(lifeTimePanel);
            this.add(comboBoxPanel);
            this.add(showAliveStudentsPanel);
            this.add(startStopButtonsPanel);

        }


    }
    public class dialogWindow extends JDialog{

        public JButton dialogOk, dialogCancel;
        public JTextArea textArea;
        dialogWindow(JFrame parent) { //диалоговое окно, которое показывает информацию о симуляции

            super(parent, "Информация о симуляции.", true );
            this.setLayout(new BorderLayout());
            this.setSize(300,300);


            JPanel buttonsPanel = new JPanel();
            dialogOk = new JButton("Ок");
            dialogCancel = new JButton("Отмена");
            dialogOk.setRequestFocusEnabled(false);
            dialogCancel.setRequestFocusEnabled(false);
            buttonsPanel.add(dialogOk);
            buttonsPanel.add(dialogCancel);

            JPanel textAreaPanel = new JPanel();
            textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setRequestFocusEnabled(false);
            textArea.setText("Информация о симуляции.");
            textAreaPanel.add(textArea);

            this.add(textAreaPanel, BorderLayout.CENTER);
            this.add(buttonsPanel, BorderLayout.SOUTH);

        }
    }
    public class showAliveStudentsDialogWindow extends JDialog {

        public JButton dialogOk;
        public JTextArea textArea;
        showAliveStudentsDialogWindow(JFrame parent) { //диалоговое окно, которое показывает информацию о симуляции

            super(parent, "Информация о живых.", true );
            this.setLayout(new BorderLayout());
            this.setSize(300,300);


            JPanel buttonsPanel = new JPanel();
            dialogOk = new JButton("Ок");
            dialogOk.setRequestFocusEnabled(false);
            buttonsPanel.add(dialogOk);

            JPanel textAreaPanel = new JPanel();
            textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setRequestFocusEnabled(false);
            textAreaPanel.add(textArea);

            this.add(textAreaPanel, BorderLayout.CENTER);
            this.add(buttonsPanel, BorderLayout.SOUTH);

        }
    }
    public class menu extends JMenuBar{

        public JMenuItem startSimulation, stopSimulation;
        menu() { //меню программы

            super();
            JMenu fileMenu = new JMenu("Управление");     // создание меню "File"

            startSimulation = new JMenuItem("Начать симуляцию");
            stopSimulation = new JMenuItem("Остановить симуляцию");
            stopSimulation.setEnabled(false);

            fileMenu.add(startSimulation);
            fileMenu.addSeparator();  	// горизонтальный разделитель
            fileMenu.add(stopSimulation);

            this.add(fileMenu);
        }
    }

    public classRoom classRoom;
    public dialogWindow dialogWindow;
    public showAliveStudentsDialogWindow showAliveStudentsDialogWindow;
    public controlPanel controlPanel;
    public menu menu;
    GUI () {

        super("Моя программа");

        classRoom = new classRoom();
        controlPanel = new controlPanel();
        dialogWindow = new dialogWindow(this);
        showAliveStudentsDialogWindow = new showAliveStudentsDialogWindow(this);
        menu = new menu();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(windowX, windowY);

        this.add(classRoom, BorderLayout.CENTER);
        this.add(controlPanel, BorderLayout.EAST);
        this.setJMenuBar(menu);

        this.setVisible(true);

        habitat = new Habitat(classRoom.getWidth(), classRoom.getHeight());

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case 66: //клавиша B - начало симуляции
                        habitat.startSimulation(controlPanel, classRoom);
                        checkAllButtons(habitat);
                        break;

                    case 69: //клавиша  E - конец симуляции
                        habitat.stopSimulation(controlPanel.showSimulationTimePanel.timeInfo, dialogWindow, classRoom);
                        checkAllButtons(habitat);
                        break;
                    case 84: //клавиша T - показывание информации

                        habitat.showTimeSimulation(controlPanel.showSimulationTimePanel);
                        controlPanel.showSimulationTimeInfoPanel.showSimulationTimeCheckBox.setSelected(habitat.infoVision);
                        break;

                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });

        controlPanel.showDialogPanel.showDialogTrue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                habitat.infoAfterFinishVisible = true;
                System.out.println("Диалоговое окно будет показано.");
            }
        });
        controlPanel.showDialogPanel.showDialogFalse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                habitat.infoAfterFinishVisible = false;
                System.out.println("Диалоговое окно не будет показано.");
            }
        });
        controlPanel.showSimulationTimeInfoPanel.showSimulationTimeCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                habitat.showTimeSimulation(controlPanel.showSimulationTimePanel);
            }
        });
        ActionListener startSimulation = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                habitat.startSimulation(controlPanel, classRoom);
                checkAllButtons(habitat);
                focusMainWindow();
            }
        };
        ActionListener stopSimulation = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                habitat.stopSimulation(controlPanel.showSimulationTimePanel.timeInfo, dialogWindow, classRoom);
                checkAllButtons(habitat);
            }
        };
        controlPanel.startStopButtonsPanel.startButton.addActionListener(startSimulation);
        controlPanel.startStopButtonsPanel.stopButton.addActionListener(stopSimulation);
        KeyListener EnterKeyListener = new KeyListener() { // при нажатии на клавишу Enter - фокус сбрасывается
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    focusMainWindow();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        };
        controlPanel.birthPeriodPanel.maleStudentsSetPeriod.addKeyListener(EnterKeyListener);
        controlPanel.birthPeriodPanel.femaleStudentsSetPeriod.addKeyListener(EnterKeyListener);
        controlPanel.lifeTimePanel.maleStudentsSetLifeTime.addKeyListener(EnterKeyListener);
        controlPanel.lifeTimePanel.femaleStudentsSetLifeTime.addKeyListener(EnterKeyListener);
        controlPanel.showAliveStudentsPanel.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                TreeMap<Integer, Long> treeMap = habitat.singleton.getTreeMap();
                String text = "";
                int count = treeMap.size();
                for(Map.Entry e : treeMap.entrySet()) {
                    if (count == 1) {
                        text+= "id: " + e.getKey() + " Время смерти: " + ((long) e.getValue()) /1000 + " с";
                    }
                    else text+= "id: " + e.getKey() + " Время смерти: " + ((long) e.getValue()) /1000 + " с\n";
                    count--;
                }
                showAliveStudentsDialogWindow.textArea.setText(text);
                showAliveStudentsDialogWindow.setVisible(true);
            }
        });

        showAliveStudentsDialogWindow.dialogOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showAliveStudentsDialogWindow.setVisible(false);
            }
        });
        showAliveStudentsDialogWindow.dialogOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showAliveStudentsDialogWindow.setVisible(false);
            }
        });

        dialogWindow.dialogOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dialogWindow.setVisible(false);
                habitat.simulation = false;
                habitat.zero();
                cleanClassRoom();
            }
        });
        dialogWindow.dialogCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dialogWindow.setVisible(false);
                habitat.simulation = true;
                habitat.timer[0].schedule(new TimerTask() {
                    @Override
                    public void run() {
                        habitat.timeFromBeginning += habitat.period;
                        habitat.update(habitat.timeFromBeginning, classRoom);
                        controlPanel.showSimulationTimePanel.timeInfo.setText("Время симуляции: " + habitat.timeFromBeginning/1000 + "с");
                    }
                }, habitat.period, habitat.period);
            }
        });



        menu.startSimulation.addActionListener(startSimulation);
        menu.stopSimulation.addActionListener(stopSimulation);


        focusMainWindow();


    }
    public void cleanClassRoom () {
        classRoom.repaint();
    }
    void checkAllButtons(Habitat habitat) {
        controlPanel.startStopButtonsPanel.startButton.setEnabled(!habitat.simulation);
        controlPanel.startStopButtonsPanel.stopButton.setEnabled(habitat.simulation);
        menu.startSimulation.setEnabled(!habitat.simulation);
        menu.stopSimulation.setEnabled(habitat.simulation);
    }
    public void focusMainWindow() { // фокус на окноо
        this.requestFocus();
    }


}

class Singleton { // паттерн - Singleton

    private static Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null)
            instance = new Singleton();
        return instance;
    }

    private LinkedList<student> students = new LinkedList<>(); // массив со студентами
    private HashSet<Integer> studentsID = new HashSet<>(); // идентификаторы "живых" объектов
    private TreeMap<Integer, Long> timeOfDeath = new TreeMap<>(); // время смерти объектор, ключ - строка-идентиификатор из HashSet, значение - время смерти Long

    public void addStudent(student student) {

        students.add(student);
        studentsID.add(student.studentID);
        timeOfDeath.put(student.studentID, student.timeOfDeath);
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

        ArrayList<Integer> indexesForDelete = new ArrayList<>();

        for(Map.Entry e : timeOfDeath.entrySet()) {
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
    public void zero() {
        students.clear();
        studentsID.clear();
        timeOfDeath.clear();
    }
}

class Habitat { //класс среды с хранящимися данными

    final Timer[] timer = {new Timer()};
    int N1, N2; // время генерации студентов и студенток соответсвенно
    int lifeTimeMale, lifeTimeFemale; // время жизни студентов и студенток соответственно
    double P1, P2; // вероятность генерации студентов и студенток соответсвенно

    int windowX, windowY; //размеры области экрана

    Singleton singleton =  Singleton.getInstance();

    int numberOfStudents = 0; //количество студентов
    int maleStudents = 0, femaleStudents = 0;
    int studentID = 0; // id следующего студента

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
                singleton.addStudent(new maleStudent((int) (Math.random() * windowX), (int) (Math.random() * windowY), timeFromBeginning, timeFromBeginning + lifeTimeMale * 1000, studentID++));
                student student = singleton.getStudent(numberOfStudents);
                maleStudents++;
                numberOfStudents++;
                System.out.println("Студент сгенерирован");
            }
        }
        if (timeFromBeginning % (N2*1000) == 0) { //попытка генерации студентки
            double random = Math.random();
            if (0 < random & random < P2) {
                singleton.addStudent(new femaleStudent((int) (Math.random() * windowX), (int) (Math.random() * windowY), timeFromBeginning, timeFromBeginning + lifeTimeFemale * 1000, studentID++));
                student student = singleton.getStudent(numberOfStudents);
                femaleStudents++;
                numberOfStudents++;
                System.out.println("Студентка сгенерирована");
            }
        }
        singleton.checkList(timeFromBeginning, this);
        drawAll(classRoom);


    }

    public void drawAll(JPanel classRoom) {

        Graphics g = classRoom.getGraphics();
        g.clearRect(0,0, classRoom.getWidth(), classRoom.getHeight());
        LinkedList<student> myClass = singleton.getStudents();
        for (int i = 0; i < myClass.size(); i++) {
            student student = myClass.get(i);
            if (student instanceof maleStudent) {
                g.drawImage(maleStudent.maleStudentImage, student.x, student.y, null);
            }
            else g.drawImage(femaleStudent.femaleStudentImage, student.x, student.y, null);
        }
    }
    public void startSimulation(GUI.controlPanel controlPanel, JPanel classRoom) {

        if (!simulation) {
            N1 = controlPanel.birthPeriodPanel.getMalePeriod();
            N2 = controlPanel.birthPeriodPanel.getFemalePeriod();
            lifeTimeMale = controlPanel.lifeTimePanel.getMaleLifeTime();
            lifeTimeFemale = controlPanel.lifeTimePanel.getFemaleLifeTime();
            P1 = controlPanel.comboBoxPanel.maleChance();
            P2 = controlPanel.comboBoxPanel.femaleChance();
            period = gcd(N1*1000, N2*1000);

            simulation = true;
            System.out.println("Нажата клавиша B или нажата кнопка 'Старт', симуляция началась");
            //начало симуляции

            timer[0].schedule(new TimerTask() {
                @Override
                public void run() {
                    timeFromBeginning += 1000;
                    update(timeFromBeginning, classRoom);
                    controlPanel.showSimulationTimePanel.timeInfo.setText("Время симуляции: " + timeFromBeginning/1000 + "с");
                }
            }, 1000, 1000);
        }
        else System.out.println("Нельзя начать симуляцию - симуляция идёт.");
    }
    public void stopSimulation(JLabel timeInfo, GUI.dialogWindow dialog, GUI.classRoom classRoom) {

        if (simulation) {

            int simulationTime = (int) timeFromBeginning/1000;
            timer[0].purge();
            timer[0].cancel();
            timer[0] = new Timer();
            timeInfo.setText("Симуляция остановлена.");
            dialog.textArea.setText("Время симуляции:"  + simulationTime + "с\nКоличество студентов: " + maleStudents + "\nКоличество студенток: " + femaleStudents);
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
        infoVision = !infoVision;
        showSimulationTimePanel.setVisible(infoVision);
    }
    public void zero() {
        singleton.zero();
        numberOfStudents = 0;
        maleStudents = 0;
        femaleStudents = 0;
        timeFromBeginning = 0;

    }
}

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
    final static Image maleStudentImage = new ImageIcon("lab3\\img\\maleStudent.jpg").getImage();
    maleStudent(int x, int y, long timeOfBirth, long timeOfDeath, int studentID) {
        super(x, y, timeOfBirth, timeOfDeath, studentID);
    }
}

class femaleStudent extends student { //класс студента женского пола
    final static Image femaleStudentImage = new ImageIcon("lab3\\img\\femaleStudent.jpg").getImage();
    femaleStudent(int x, int y, long timeOfBirth, long timeOfDeath, int studentID) {
        super(x, y, timeOfBirth, timeOfDeath, studentID);
    }
}