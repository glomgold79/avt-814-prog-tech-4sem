
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicLong;

class GUI extends JFrame {
    private boolean isTimerRunning = false,
            isTimeVisible = false,
            isStatisticsVisible = false,
            isShowModalDialog = false,
            isStatShow, isShowSimTime;

    static boolean stopDroneAI = true,
                   stopWorkerAI = true;

    private WorkerAI workerMoving;
    private DroneAI droneMoving;

    private AtomicLong timeSum = new AtomicLong(0);
    private Timer timer;
    private long time;

    static JComboBox<String> probComboBox;
    static JSlider slider;
    private JCheckBox statShow;
    private JRadioButton showSimTime;

    private void start() {
        Habitat.array.clear();
        Habitat.identifiers.clear();
        Habitat.birthdays.clear();
        Habitat.numberOfDrones = 0;
        Habitat.numberOfWorkers = 0;

        workerMoving = new WorkerAI();
        droneMoving = new DroneAI();
        Thread droneThread = new Thread(droneMoving);
        Thread workerThread = new Thread(workerMoving);
        droneThread.start();
        workerThread.start();
        BaseAI.move = true;

        timer = new Timer();
        isTimerRunning = true;
        time = 0; // аннулируем время которое передаем в update()
        timeSum.set(0); // аннулируем время которое отображается на экране
    }

    private int stop() {
        BaseAI.move = false;
        Object[] options = {
                "ОК",
                "Отмена"};
        TextArea statTextArea = new TextArea("Время симуляции: " + timeSum + " с \nИтого пчел: " +
                (Habitat.numberOfDrones + Habitat.numberOfWorkers) +
                "\nТрутней: " + Habitat.numberOfDrones + "\nРабочих: " + Habitat.numberOfWorkers);
        statTextArea.setEditable(false);
        statTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        statTextArea.setPreferredSize(new Dimension(200, 100));
        return JOptionPane.showOptionDialog(null,
                statTextArea,
                "Результаты симуляции",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                null);
    }

    private void _continue() {
        timer = new Timer();
        isTimerRunning = true;
    }

    void saveConfig() throws IOException {
        FileOutputStream outputStream = new FileOutputStream("data/config.txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(statShow.isSelected() + "," + showSimTime.isSelected()
                + "," + Habitat.N1 + "," + Habitat.N2 + ","
                + Habitat.P + "," + Habitat.K + "," + Habitat.droneTimeOfLife + "," + Habitat.WorkerTimeOfLife);
        System.out.println("Config saved.");
        objectOutputStream.close();
    }

    void loadConfig() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("data/config.txt");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        String config = objectInputStream.readObject().toString();
        String[] parts = config.split(",", 8);
        for (int i = 0; i < 8; i++) {
            System.out.println(parts[i]);
        }

        isStatShow = Boolean.valueOf(parts[0]);
        isShowSimTime = Boolean.valueOf(parts[1]);
        Habitat.N1 = Integer.valueOf(parts[2]);
        Habitat.N2 = Integer.valueOf(parts[3]);
        Habitat.P = Integer.valueOf(parts[4]);
        Habitat.K = Integer.valueOf(parts[5]);
        Habitat.droneTimeOfLife = Integer.valueOf(parts[6]);
        Habitat.WorkerTimeOfLife = Integer.valueOf(parts[7]);
    }






    GUI() {
        super("Bee's area");
        final int DEFAULT_X = 1500; // первоначальные размеры фрейма
        final int DEFAULT_Y = 1000;
        this.setSize(DEFAULT_X, DEFAULT_Y);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // завершение программы при закрытии фрейма
        this.setMinimumSize(new Dimension(450, 600));
        this.setIconImage(new ImageIcon("Icon.png").getImage());
        this.setLocationRelativeTo(null); // фрейм в центре экрана
        this.setFocusable(true); // фокус на фрейме при запуске
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    saveConfig();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                System.exit(0);
            }
        });


        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        try {
            loadConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // глобальный контейнер
        JPanel scene = new JPanel();
        add(scene);
        scene.setLayout(new BorderLayout());

        // панель управления симуляцией
        JPanel control = new JPanel();
        Dimension controlSize = new Dimension(225, getHeight());
        control.setPreferredSize(controlSize);
        control.setLayout(new FlowLayout(FlowLayout.CENTER));
        control.setBackground(new Color(232, 232, 232));
        scene.add(control, BorderLayout.EAST);

        // панель визуализации объектов
        int visualX = getWidth() - controlSize.width - 17;
        int visualY = getHeight();
        VPanel visual = new VPanel(visualX, visualY);
        scene.add(visual, BorderLayout.WEST);

        // время симуляции
        JLabel timeModule = new JLabel("Время: " + timeSum + " с");
        timeModule.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        visual.add(timeModule);
        timeModule.setVisible(false);

        // статистика симуляции
        JLabel statistics = new JLabel();
        statistics.setFont(new Font("Arial", Font.PLAIN, 25));
        statistics.setBackground(visual.getBackground());
        visual.add(statistics);

        // создание рабочей области
        Habitat habitat = new Habitat(visualX, visualY);

        class SimulationTask extends TimerTask {
            private long time;
            private Habitat simulation;

            SimulationTask(Habitat simulation, long time) {
                this.simulation = simulation;
                this.time = time;
            }

            @Override
            public void run() {
                timeSum.set(timeSum.get() + 1);
                simulation.update(time); // обновление рабочей области
                time++;
                repaint();
                timeModule.setText("Время: " + timeSum.get() + " с");
            }
        }

        this.addKeyListener(new KeyAdapter() {
            Timer timer;
            long time;
            SimulationTask updating = null;

            @Override
            public void keyPressed(KeyEvent evt) {

                char event = evt.getKeyChar();
                if (event == 'и' || event == 'B' || event == 'И') event = 'b';
                if (event == 'у' || event == 'E' || event == 'У') event = 'e';
                if (event == 'е' || event == 'T' || event == 'Е') event = 't';

                switch (event) {
                    case 'b':
                        if (!isTimerRunning) { // без условия таймер бы ускорялся
                            repaint(); // очистка фрейма перерисовкой
                            if (isStatisticsVisible) {
                                statistics.setVisible(false);
                                isStatisticsVisible = false;
                            }

                            Habitat.numberOfDrones = 0;
                            Habitat.numberOfWorkers = 0;
                            timer = new Timer();
                            isTimerRunning = true;
                            time = 0; // аннулируем время которое передаем в update()
                            timeSum.set(0); // аннулируем время которое отображается на экране
                            updating = new SimulationTask(habitat, time);
                            timer.schedule(updating, 0, 1000);
                        }
                        break;
                    case 'e':
                        if (isTimerRunning) {
                            timer.cancel();
                            isTimerRunning = false;
                        }
                        if (!isStatisticsVisible) {
                            if (isTimeVisible) {
                                timeModule.setVisible(false);
                                isTimeVisible = false;
                            }

                            statistics.setText("<html>Время симуляции: " + timeSum + " с <br>Итого пчел: " +
                                    (Habitat.numberOfDrones + Habitat.numberOfWorkers) +
                                    "<br>Трутней: " + Habitat.numberOfDrones + "<br>Рабочих: " + Habitat.numberOfWorkers);
                            statistics.setVisible(true);
                            repaint(); // очистка фрейма перерисовкой
                            isStatisticsVisible = true;
                        }
                        break;
                    case 't':
                        if (!isStatisticsVisible) {
                            if (!isTimeVisible) {
                                timeModule.setVisible(true);
                                isTimeVisible = true;
                            } else {
                                timeModule.setVisible(false);
                                isTimeVisible = false;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        // кнопки старт и стоп
        Dimension buttonSize = new Dimension(150, 40);
        JButton startButton = new JButton("Старт");
        startButton.setPreferredSize(buttonSize);
        startButton.addActionListener(new ActionListener() {
            SimulationTask updating = null;

            public void actionPerformed(ActionEvent e) {
                if (!isTimerRunning) { // без условия таймер бы ускорялся
                    if (isStatisticsVisible) {
                        statistics.setVisible(false);
                        isStatisticsVisible = false;
                    }
                    start();
                    updating = new SimulationTask(habitat, time);
                    timer.schedule(updating, 0, 1000);
                }
                requestFocus();
            }
        });
        control.add(startButton);

        JButton stopButton = new JButton("Стоп");
        stopButton.setPreferredSize(buttonSize);
        stopButton.addActionListener(new ActionListener() {
            SimulationTask updating = null;

            public void actionPerformed(ActionEvent e) {
                if (isTimerRunning) {
                    timer.cancel();
                    isTimerRunning = false;
                }
                if (isShowModalDialog) {
                    int response = stop();
                    switch (response) {
                        case JOptionPane.YES_OPTION:
                            System.out.println("JOptionPane.YES_Option");
                            Habitat.array.clear(); // очистка коллекции
                            repaint();
                            break;
                        case JOptionPane.NO_OPTION:
                            System.out.println("JOptionPane.NO_Option");
                            _continue();
                            updating = new SimulationTask(habitat, time);
                            timer.schedule(updating, 0, 1000);
                    }
                } else {
                    Habitat.array.clear(); // очистка коллекции
                    repaint();
                }
                requestFocus();
            }
        });
        control.add(stopButton);

        // флажок показывать информацию. объединена в группу кнопок с ее дублированным вариантом в меню
        statShow = new JCheckBox("Показывать информацию");
        statShow.setSelected(isStatShow);
        statShow.setBackground(control.getBackground());
        statShow.addActionListener(e -> {
            System.out.println("Information is shown/hidden.");
            isShowModalDialog = !isShowModalDialog; // меняем состояние булеаны
            requestFocus();
        });
        control.add(statShow);

        // панель переключателей отображения времени симуляции
        JPanel timeRadioButtonPanel = new JPanel(new GridLayout(2, 1));
        timeRadioButtonPanel.setBackground(control.getBackground());
        control.add(timeRadioButtonPanel);

        // переключатели отображения времени симуляции
        showSimTime = new JRadioButton("Показывать время симуляции");
        timeModule.setVisible(isShowSimTime);
        showSimTime.addActionListener(e -> {
            System.out.println("Simulation time is shown.");
            timeModule.setVisible(true);
            isTimeVisible = true;
            requestFocus();
        });
        JRadioButton hideSimTime = new JRadioButton("Скрывать время симуляции");
        hideSimTime.addActionListener(e -> {
            System.out.println("Simulation time is hidden.");
            timeModule.setVisible(false);
            isTimeVisible = false;
            requestFocus();
        });
        showSimTime.setBackground(control.getBackground());
        hideSimTime.setBackground(control.getBackground());
        timeRadioButtonPanel.add(showSimTime);
        timeRadioButtonPanel.add(hideSimTime);

        // связываем переключатели отображения времени симуляции в группу
        ButtonGroup timeGroup = new ButtonGroup();
        timeGroup.add(showSimTime);
        timeGroup.add(hideSimTime);
        timeGroup.setSelected(hideSimTime.getModel(), true); // при запуске программы выбрано "Скрывать время симуляции"

        // панель периода рождения объектов
        JPanel periodPanel = new JPanel();
        periodPanel.setLayout(new GridLayout(3, 1));
        periodPanel.setBackground(control.getBackground());
        control.add(periodPanel);

        JLabel periodHint = new JLabel("Периоды рождения:");
        periodPanel.add(periodHint);

        // панель для размещения текстовых полей
        JPanel periodEditPanel = new JPanel();
        periodEditPanel.setLayout(new BoxLayout(periodEditPanel, BoxLayout.X_AXIS));
        periodEditPanel.setBackground(control.getBackground());
        periodPanel.add(periodEditPanel);

        JPanel dronePeriodPanel = new JPanel(new GridLayout(2, 1));
        dronePeriodPanel.setBackground(control.getBackground());
        periodEditPanel.add(dronePeriodPanel);

        JLabel droneHint = new JLabel("Трутень:");
        dronePeriodPanel.add(droneHint);

        // поля ввода пользователем периода рождения
        JTextField periodDrone = new JTextField(Integer.toString(Habitat.N1));
        periodDrone.addActionListener(e -> {
            try {
                Habitat.N1 = Integer.valueOf(periodDrone.getText());
                System.out.println("New period of drones: " + Habitat.N1);
                if (Habitat.N1 < 0)
                    throw new Exception();
            } catch (Exception e1) {
                Habitat.N1 = 4;
                periodDrone.setText(Integer.toString(Habitat.N1));
                JOptionPane.showMessageDialog(null,
                        "Введено неверное значение. Введите корректный период рождения.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
        dronePeriodPanel.add(periodDrone);

        JPanel WorkerPeriodPanel = new JPanel(new GridLayout(2, 1));
        WorkerPeriodPanel.setBackground(control.getBackground());
        periodEditPanel.add(WorkerPeriodPanel);

        JLabel WorkerHint = new JLabel("Рабочий:");
        WorkerPeriodPanel.add(WorkerHint);

        JTextField periodWorker = new JTextField(Integer.toString(Habitat.N2));
        periodWorker.addActionListener(e -> {
            try {
                Habitat.N2 = Integer.valueOf(periodWorker.getText());
                System.out.println("New period of Workers: " + Habitat.N2);
                if (Habitat.N2 < 0)
                    throw new Exception();
            } catch (Exception e1) {
                Habitat.N2 = 3;
                periodWorker.setText(Integer.toString(Habitat.N2));
                JOptionPane.showMessageDialog(null,
                        "Введено неверное значение. Введите корректный период рождения.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
        WorkerPeriodPanel.add(periodWorker);

        // панель с компонентами, связанными с вероятностью рождения
        JPanel probabilityPanel = new JPanel(new GridLayout(4, 1));
        probabilityPanel.setBackground(control.getBackground());
        control.add(probabilityPanel);

        // варианты в JComboBox
        String[] items = {
                "0",
                "10",
                "20",
                "30",
                "40",
                "50",
                "60",
                "70",
                "80",
                "90",
                "100"
        };

        // вероятность трутня регулируется с помощью JComboBox
        probabilityPanel.add(new JLabel("Процент трутней:"));
        probComboBox = new JComboBox<>(items);
        probComboBox.setSelectedItem(Integer.toString(Habitat.K));
        probComboBox.addActionListener(e -> {
            Habitat.K = Integer.valueOf(items[probComboBox.getSelectedIndex()]);
            System.out.println("New probability of drones: " + Habitat.K);
            requestFocus();
        });
        probabilityPanel.add(probComboBox);

        // вероятность рабочих регулируется с помощью JSlider
        probabilityPanel.add(new JLabel("Вероятность рождения рабочего:"));
        slider = new JSlider(0, 100, Habitat.P);
        slider.setBackground(control.getBackground());
        slider.setMajorTickSpacing(10); // шаг 10 единиц
        slider.setPaintLabels(true); // отображение числовых меток (шаг 10 ед.)
        slider.setPaintTicks(true); // отображение засечек
        slider.addChangeListener(e -> {
            Habitat.P = slider.getValue();
            System.out.println("New probability of workers: " + Habitat.P);
            requestFocus();
        });
        probabilityPanel.add(slider);

        // панель времени жизни
        JPanel lifePanel = new JPanel();
        lifePanel.setLayout(new GridLayout(3, 1));
        lifePanel.setBackground(control.getBackground());
        control.add(lifePanel);

        JLabel lifeHint = new JLabel("Время жизни пчел:");
        lifePanel.add(lifeHint);

        // панель для размещения текстовых полей
        JPanel lifeEditPanel = new JPanel();
        lifeEditPanel.setLayout(new BoxLayout(lifeEditPanel, BoxLayout.X_AXIS));
        lifeEditPanel.setBackground(control.getBackground());
        lifePanel.add(lifeEditPanel);

        JPanel DroneLifePanel = new JPanel(new GridLayout(2, 1));
        DroneLifePanel.setBackground(control.getBackground());
        lifeEditPanel.add(DroneLifePanel);

        JLabel droneLifeHint = new JLabel("Трутень:");
        DroneLifePanel.add(droneLifeHint);

        // поля ввода пользователем периода рождения
        JTextField lifeDrone = new JTextField(Integer.toString(Habitat.droneTimeOfLife));
        lifeDrone.addActionListener(e -> {
            try {
                Habitat.droneTimeOfLife = Integer.valueOf(lifeDrone.getText());
                System.out.println("New lifetime of drones: " + Habitat.droneTimeOfLife);
                if (Habitat.droneTimeOfLife < 0)
                    throw new Exception();
            } catch (Exception e1) {
                Habitat.droneTimeOfLife = 4;
                lifeDrone.setText(Integer.toString(Habitat.droneTimeOfLife));
                JOptionPane.showMessageDialog(null,
                        "Введено неверное значение. Введите корректное время жизни.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
        DroneLifePanel.add(lifeDrone);

        JPanel WorkerLifePanel = new JPanel(new GridLayout(2, 1));
        WorkerLifePanel.setBackground(control.getBackground());
        lifeEditPanel.add(WorkerLifePanel);

        JLabel WorkerLifeHint = new JLabel("Рабочий:");
        WorkerLifePanel.add(WorkerLifeHint);

        JTextField lifeWorker = new JTextField(Integer.toString(Habitat.WorkerTimeOfLife));
        lifeWorker.addActionListener(e -> {
            try {
                Habitat.WorkerTimeOfLife = Integer.valueOf(lifeWorker.getText());
                System.out.println("New lifetime of workers: " + Habitat.WorkerTimeOfLife);
                if (Habitat.WorkerTimeOfLife < 0)
                    throw new Exception();
            } catch (Exception e1) {
                Habitat.WorkerTimeOfLife = 5;
                lifeWorker.setText(Integer.toString(Habitat.WorkerTimeOfLife));
                JOptionPane.showMessageDialog(null,
                        "Введено неверное значение. Введите корректное время жизни.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
        WorkerLifePanel.add(lifeWorker);

        // кнопка текущие объекты
        JButton currentObjectsButton = new JButton("Список объектов");
        currentObjectsButton.setPreferredSize(buttonSize);
        currentObjectsButton.addActionListener(e -> {
            String currentObjectsText = "";
            for (int i = 0; i < Habitat.array.size(); i++) {
                String type;
                int number = i + 1;
                if (Habitat.array.get(i) instanceof Drone) {
                    type = "трутень";
                } else type = "рабочий";
                long lifetime = Habitat.birthdays.get(Habitat.array.get(i).id);
                int id = Habitat.array.get(i).id;
                currentObjectsText += number + ".\nТип: " + type + "\nВремя рождения: " +
                        lifetime + "\nИдентификатор: " + id + "\n";
            }
            if (Habitat.array.isEmpty()) currentObjectsText = "Объекты не сгенерированы.";
            TextArea currentObjects = new TextArea(currentObjectsText);
            currentObjects.setEditable(false);
            JOptionPane.showMessageDialog(this,
                    currentObjects,
                    "Текущие объекты",
                    JOptionPane.INFORMATION_MESSAGE);
            requestFocus();
        });
        control.add(currentObjectsButton);







////////////////////////////////////////
        JPanel AIPanel = new JPanel(new GridLayout(1, 2));
        AIPanel.setBackground(control.getBackground());
        control.add(AIPanel);

        JButton droneAIButton = new JButton("ИИ трутней");
        droneAIButton.addActionListener(e -> {
            synchronized (droneMoving.tmp) {
                if (!stopDroneAI) {
                    stopDroneAI = true;
                } else {
                    stopDroneAI = false;
                    droneMoving.tmp.notify();
                }
            }
            requestFocus();
        });
        AIPanel.add(droneAIButton);

        JButton workerAIButton = new JButton("ИИ рабочих");
        workerAIButton.addActionListener(e -> {
            synchronized (workerMoving.tmp) {
                if (!stopWorkerAI) {
                    stopWorkerAI = true;
                } else {
                    stopWorkerAI = false;
                    workerMoving.tmp.notify();
                }
            }
            requestFocus();
        });
        AIPanel.add(workerAIButton);

        String[] priorityItems = {
                "ИИ трутня",
                "ИИ рабочего",
        };

        JPanel priorityPanel = new JPanel(new GridLayout(3, 1));
        priorityPanel.setBackground(control.getBackground());
        control.add(priorityPanel);

        priorityPanel.add(new JLabel("Приоритеты:"));
        JComboBox<String> priorityComboBox = new JComboBox<>(priorityItems);
        priorityComboBox.addActionListener(e -> {
            if (Objects.equals(priorityComboBox.getSelectedItem(), "ИИ трутня")) {
                droneMoving.setPriority(BaseAI.priority);
                System.out.println("Priority drone AI: " + BaseAI.priority);
            }
            if (Objects.equals(priorityComboBox.getSelectedItem(), "ИИ рабочего")) {
                workerMoving.setPriority(BaseAI.priority);
                System.out.println("Priority worker AI: " + BaseAI.priority);
            }
            requestFocus();
        });
        priorityPanel.add(priorityComboBox);

        JTextField priorityTextField = new JTextField(Integer.toString(BaseAI.priority));
        priorityTextField.addActionListener(e -> {
            try {
                BaseAI.priority = Integer.valueOf(priorityTextField.getText());
                System.out.println("priority: " + BaseAI.priority);
                if (Integer.valueOf(priorityTextField.getText()) <= 0 || Integer.valueOf(priorityTextField.getText()) > 10)
                    throw new Exception();
            } catch (Exception e1) {
                BaseAI.priority = 1;
                priorityTextField.setText(Integer.toString(BaseAI.priority));
                JOptionPane.showMessageDialog(null,
                        "Введено неверное значение. Введите корректный приоритет.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
        priorityPanel.add(priorityTextField);


        JButton consoleButton = new JButton("Консоль");
        consoleButton.setPreferredSize(buttonSize);
        consoleButton.addActionListener(e -> new Console(this));
        control.add(consoleButton);











        /*
         *
         *  МЕНЮ
         *
         * */


        JMenuBar menuBar = new JMenuBar();
        {
            JMenu menuFile = new JMenu("Файл");
            menuBar.add(menuFile);


            JMenuItem saveItem = new JMenuItem("Сохранить");
            menuFile.add(saveItem);
            saveItem.addActionListener(e -> {

                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream("data/save.dat");
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                ObjectOutputStream objectOutputStream = null;
                try {
                    objectOutputStream = new ObjectOutputStream(outputStream);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                // сохраняем в файл
                try {
                    if (objectOutputStream != null) {
                        objectOutputStream.writeObject(Habitat.array);
                        objectOutputStream.writeObject(Habitat.identifiers);
                        objectOutputStream.writeObject(Habitat.birthdays);
                        System.out.println("Saved successfully!");
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                try {
                    if (objectOutputStream != null) {
                        objectOutputStream.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });

            JMenuItem loadItem = new JMenuItem("Загрузить");
            menuFile.add(loadItem);
            loadItem.addActionListener(e -> {
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream("data/save.dat");
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                ObjectInputStream objectInputStream = null;
                try {
                    objectInputStream = new ObjectInputStream(fileInputStream);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                try {
                    if (objectInputStream != null) {
                        Habitat.array.clear();
                        Habitat.numberOfDrones = 0;
                        Habitat.numberOfWorkers = 0;
                        time = 0;
                        Habitat.array = (ArrayList<Bee>) objectInputStream.readObject();
                        for (int i = 0; i < Habitat.array.size(); i++) {
                            Habitat.array.get(i).timeOfBirth = 0;
                        }
                    }
                } catch (IOException | ClassNotFoundException e1) {
                    e1.printStackTrace();
                }

                try {
                    if (objectInputStream != null) {
                        Habitat.identifiers = (HashSet<Integer>) objectInputStream.readObject();
                    }
                } catch (IOException | ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
                int mapSize = Habitat.array.size();
                Habitat.birthdays = new TreeMap<>();
                for (int i = 0; i < mapSize; i++) {
                    Habitat.birthdays.put(Habitat.array.get(i).id, 0L);
                }
                for (int i = 0; i < Habitat.array.size(); i++) {
                    if (Habitat.array.get(i) instanceof Drone) {
                        Habitat.numberOfDrones++;
                    } else {
                        Habitat.numberOfWorkers++;
                    }
                }
            });






            JMenu menuSimulation = new JMenu("Управление");
            menuBar.add(menuSimulation);

            JMenuItem startMenuItem = new JMenuItem("Старт");
            startMenuItem.addActionListener(new ActionListener() {
                SimulationTask updating = null;

                public void actionPerformed(ActionEvent e) {
                    if (!isTimerRunning) {
                        if (isStatisticsVisible) {
                            statistics.setVisible(false);
                            isStatisticsVisible = false;
                        }
                        start();
                        updating = new SimulationTask(habitat, time);
                        timer.schedule(updating, 0, 1000);
                    }
                    requestFocus();
                }
            });
            menuSimulation.add(startMenuItem);

            JMenuItem stopMenuItem = new JMenuItem("Стоп");
            stopMenuItem.addActionListener(new ActionListener() {
                SimulationTask updating = null;

                public void actionPerformed(ActionEvent e) {
                    if (isTimerRunning) {
                        timer.cancel();
                        isTimerRunning = false;
                    }
                    if (isShowModalDialog) {
                        int response = stop();
                        switch (response) {
                            case JOptionPane.YES_OPTION:
                                System.out.println("JOptionPane.YES_Option");
                                Habitat.array.clear(); // очистка фрейма
                                repaint(); // перерисовка очищенного фрейма
                                break;
                            case JOptionPane.NO_OPTION:
                                System.out.println("JOptionPane.NO_Option");
                                _continue();
                                updating = new SimulationTask(habitat, time);
                                timer.schedule(updating, 0, 1000);
                        }
                    } else {
                        Habitat.array.clear(); // очистка коллекции
                        repaint();
                    }
                    requestFocus();
                }
            });
            menuSimulation.add(stopMenuItem);

            JMenu menuInfo = new JMenu("Информация");
            menuBar.add(menuInfo);

            JCheckBoxMenuItem statItem = new JCheckBoxMenuItem("Показывать информацию");
            statItem.addActionListener(e -> {
                System.out.println("Information is shown/hidden.");
                isShowModalDialog = !isShowModalDialog; // меняем состояние булеаны
            });
            menuInfo.add(statItem);

            statItem.addActionListener(e -> { // синхронизация продублированных кнопок "показать информацию"
                if (statItem.isSelected() && !statShow.isSelected()) {
                    statShow.setSelected(true);
                }
                if (!statItem.isSelected() && statShow.isSelected()) {
                    statShow.setSelected(false);
                }
            });
            statShow.addActionListener(e -> {
                if (statShow.isSelected() && !statItem.isSelected()) {
                    statItem.setSelected(true);
                }
                if (!statShow.isSelected() && statItem.isSelected()) {
                    statItem.setSelected(false);
                }
            });

            menuInfo.addSeparator();

            JRadioButtonMenuItem showSimTimeItem = new JRadioButtonMenuItem("Показывать время симуляции", false);
            showSimTimeItem.addActionListener(e -> {
                System.out.println("Simulation time is shown.");
                timeModule.setVisible(true);
                isTimeVisible = true;
                requestFocus();
            });
            menuInfo.add(showSimTimeItem);

            JRadioButtonMenuItem hideSimTimeItem = new JRadioButtonMenuItem("Скрывать время симуляции", true);
            hideSimTimeItem.addActionListener(e -> {
                System.out.println("Simulation time is hidden.");
                timeModule.setVisible(false);
                isTimeVisible = false;
                requestFocus();
            });
            menuInfo.add(hideSimTimeItem);

            ButtonGroup timeItemGroup = new ButtonGroup();
            timeItemGroup.add(showSimTimeItem);
            timeItemGroup.add(hideSimTimeItem);

        }
        setJMenuBar(menuBar);

        this.setVisible(true);
    }


}