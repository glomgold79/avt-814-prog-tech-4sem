import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class GUI extends JFrame {

    BufferedImage offScreenImage;
    boolean flag_is_work = false;
    private Timer timer;
    private static BufferedImage BackgroundImg;
    public long cumulativePeriod = 0;
    private final long period = 1;
    private AlbinoAI albinoAI = new AlbinoAI();
    private OrdinaryAI ordinaryAI = new OrdinaryAI();

    private Client client;

    Console console;

    JPanel jPanelImage = new JPanel(), jPanelControl = new JPanel();
    JButton jButtonStart = new JButton("Старт"), jButtonStop = new JButton("Стоп");
    JButton jButtonObjects = new JButton("Текущие объекты");
    JButton jButtonAlbinoAI = new JButton("Движение альбиносов");
    JButton jButtonOrdinaryAI = new JButton("Движение обыкновенных");
    JLabel jLabel = new JLabel();
    JLabel jLabelK = new JLabel("Задать K:");
    JLabel jLabelP = new JLabel("Задать P:");
    JLabel jLabelNOrdinary = new JLabel("Задать N для обыкновенных:");
    JLabel jLabelNAlbino = new JLabel("Задать N для альбиносов:");
    JLabel jLabelBirthTimeOrdinary = new JLabel("Задать время жизни обыкновенных:");
    JLabel jLabelBirthTimeAlbino = new JLabel("Задать время жизни альбиносов:");
    JLabel jLabelOrdinaryPriority = new JLabel("Приоритет обыкновенных");
    JLabel jLabelAlbinoPriority = new JLabel("Приоритет альбиносов");
    JDialog jDialog = new JDialog(this,"Информация о симуляции", true);
    JDialog jDialogObjects = new JDialog(this,"Текущие объекты",true);
    JDialog jDialogConsole = new JDialog(this,"Консоль",false);
    JDialog jDialogClient = new JDialog(this, "Клиент", false);
    JTextArea jTextArea = new JTextArea();
    JTextArea jTextAreaObjects = new JTextArea();
    JTextArea jTextAreaConsole = new JTextArea();
    JButton jButtonOk = new JButton("Окей"), jButtonCancel = new JButton("Отмена");
    JComboBox<String> jComboBox = new JComboBox<>();
    JComboBox<Integer> jComboBoxOrdinaryPriority = new JComboBox<>();
    JComboBox<Integer> jComboBoxAlbinoPriority = new JComboBox<>();
    JList<String> jList = new JList<>();
    JList<String> jListOfClients = new JList<>();
    JTextField jTextFieldNOrdinary = new JTextField();
    JTextField jTextFieldNAlbino = new JTextField();
    JTextField jTextFieldLiveTimeOrdinary = new JTextField();
    JTextField jTextFieldLiveTimeAlbino = new JTextField();
    JRadioButton jRadioButtonShowTime = new JRadioButton("Показать время симуляции"),
                 jRadioButtonNoShowTime = new JRadioButton("Не показывать время симуляции");

    PipedWriter pipedWriter;
    Saver saver = new Saver(this);
    FileReader fileReader;
    Scanner scanner;

    int P, K, NA, NO, LTO, LTA, PO, PA;

    Boolean RadioButtonBoolean = false;

    public GUI(int Width, int Height) {
        try {
            fileReader = new FileReader("Configuration.txt");
            scanner = new Scanner(fileReader);
            P = scanner.nextInt();
            K = scanner.nextInt();
            NO = scanner.nextInt();
            NA = scanner.nextInt();
            LTO = scanner.nextInt();
            LTA = scanner.nextInt();
            PO = scanner.nextInt();
            PA = scanner.nextInt();
            scanner.close();
            fileReader.close();
        } catch (IOException ignore) { }
        saver.SaveConfigurations();

        pipedWriter = new PipedWriter();
        console = new Console();
        try {
            pipedWriter.connect(console.pipedReader);
        } catch (IOException ignored) { }
        albinoAI.start();
        ordinaryAI.start();

        jDialog.setSize(200,350);
        jDialog.setLocation(350,200);
        jDialog.setLayout(new GridLayout(3,1, 15,15));
        jTextArea.setEditable(false);
        jDialog.add(jTextArea);
        jDialog.add(jButtonOk);
        jDialog.add(jButtonCancel);

        jDialogObjects.setSize(300, 400);
        jDialogObjects.setLocation(350,200);
        jTextAreaObjects.setEditable(false);
        jDialogObjects.add(jTextAreaObjects);

        jDialogConsole.setSize(500, 300);
        jDialogConsole.setLocation(350,200);
        jTextAreaConsole.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        int offset = jTextAreaConsole.getLineStartOffset(jTextAreaConsole.getLineCount() - 1);
                        int length = jTextAreaConsole.getLineEndOffset(jTextAreaConsole.getLineCount() - 1) - offset + 1;
                        String string = jTextAreaConsole.getText(offset, length);
                        pipedWriter.write(string);
                        console.Start();
                    } catch (BadLocationException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        jDialogConsole.add(jTextAreaConsole);

        client = new Client();
        jDialogClient.setSize(300,400);
        jDialogClient.setLocation(100,100);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        jListOfClients.setModel(listModel);
        jListOfClients.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    client.RequestConfigs(jListOfClients.getSelectedValue());
                    try {
                        fileReader = new FileReader("Configuration.txt");
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    scanner = new Scanner(fileReader);
                    P = scanner.nextInt();
                    K = scanner.nextInt();
                    NO = scanner.nextInt();
                    NA = scanner.nextInt();
                    LTO = scanner.nextInt();
                    LTA = scanner.nextInt();
                    PO = scanner.nextInt();
                    PA = scanner.nextInt();
                    scanner.close();
                    try {
                        fileReader.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    jComboBox.setSelectedIndex(P);
                    jList.setSelectedIndex(K);
                    jTextFieldNOrdinary.setText(Integer.toString(NO));
                    jTextFieldNAlbino.setText(Integer.toString(NA));
                    jTextFieldLiveTimeOrdinary.setText(Integer.toString(LTO));
                    jTextFieldLiveTimeAlbino.setText(Integer.toString(LTA));
                    jComboBoxOrdinaryPriority.setSelectedIndex(PO);
                    jComboBoxAlbinoPriority.setSelectedIndex(PA);
                }
                if (e.getKeyCode() == KeyEvent.VK_U) {
                    listModel.removeAllElements();
                    for (String string : client.getUsersOnline())
                        listModel.addElement(string);
                }
            }
        });
        jDialogClient.add(jListOfClients);

        try {
            BackgroundImg = ImageIO.read(new File("./Images/Field.jpg"));
        } catch (IOException ignored) { }

        setTitle("Молнер Василий АВТ-814 Вариант №4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(Width, Height));
        setFocusable(true);
        setLocation(200,0);
        createMenuBar();
        createPanelUI(getContentPane());
        pack();
        setVisible(true);
        StartKeyListener();
    }

    private void StartKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_B:
                        ActionStart();
                        break;
                    case KeyEvent.VK_E:
                        ActionStop();
                        break;
                    case KeyEvent.VK_T:
                        ActionShowText();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void ActionStart() {
        Habitat.POrdinary = (double) Integer.parseInt((String) Objects.requireNonNull(jComboBox.getSelectedItem())) / 100;
        Habitat.KAlbino = (double) Integer.parseInt(jList.getSelectedValue()) / 100;
        try {
            Habitat.NOrdinary = Integer.parseInt(jTextFieldNOrdinary.getText());
            Habitat.NAlbino = Integer.parseInt(jTextFieldNAlbino.getText());
            Ordinary.TimeOfLife = Integer.parseInt(jTextFieldLiveTimeOrdinary.getText());
            Albino.TimeOfLife = Integer.parseInt(jTextFieldLiveTimeAlbino.getText());
        }
        catch (NumberFormatException ex) {
            JDialog jDialogEx = new JDialog(this,"Ошибка!",true);
            jDialogEx.setSize(320, 240);
            jDialogEx.setLayout(new GridLayout(2,1));

            JButton jButtonExOk = new JButton("Я исправлю");
            JLabel jLabelExInf = new JLabel("Неверный ввод данных");
            jDialogEx.add(jLabelExInf);
            jDialogEx.add(jButtonExOk);

            jButtonExOk.addActionListener(e -> jDialogEx.setVisible(false));

            jDialogEx.setVisible(true);
            return;
        }
        if (!flag_is_work) {
            flag_is_work = true;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Habitat.Update(cumulativePeriod);
                    if (RadioButtonBoolean) {
                        ActionShowText();
                    }
                    cumulativePeriod += period;
                }
            }, 0, period);
        }
        jButtonStop.setEnabled(true);
        jButtonStart.setEnabled(false);
    }

    private void ActionStop() {
        timer.cancel();
        flag_is_work = false;
        Singleton.getVector().clear();
        Singleton.getHashMap().clear();
        Singleton.getTreeSetID().clear();
        jButtonStop.setEnabled(false);
        jButtonStart.setEnabled(true);
        jTextArea.setText("Кроликов всего " + Rabbit.getAllQuantity() +
                "\nОбычных " + Ordinary.getOrdinaryQuantity() +
                "\nАльбиносов " + Albino.getAlbinoQuantity() +
                "\n\nВремени прошло " + cumulativePeriod + " миллисекунд");
        cumulativePeriod = 0;
        Rabbit.AllQuantity = 0;
        Ordinary.OrdinaryQuantity = 0;
        Albino.AlbinoQuantity = 0;
        jDialog.setVisible(true);
    }

    private void ActionShowText() {
        jLabel.setFont(new Font("TimesRoman", Font.BOLD, 12));
        String string = "Время " + cumulativePeriod + " миллисекунд";
        jLabel.setText(string);
        add(jLabel);
        setVisible(true);
    }

    private void ActionShowObjects() {
        StringBuilder string = new StringBuilder();
        for (Rabbit rabbit : Singleton.getVector()) {
            string.append(rabbit.ID).append(" ").append(rabbit.BirthTime).append("\n");
        }
        jTextAreaObjects.setText("Ключ/Время рождения\n" + string);
        jDialogObjects.setVisible(true);
    }

    public void Render() {
        //Двойная буфферизация. Рисую в BufferedImage, и только готовое вывожу в фрейм (устраняю мерцание)
        int w = jPanelImage.getWidth()*2, h = jPanelImage.getHeight()*2; //умножаю на 2, потому что падает качество
        offScreenImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics offScreenGraphics= offScreenImage.getGraphics();

        offScreenGraphics.drawImage(BackgroundImg, 0,0, w, h,null);
        for (int i = 0; i < Singleton.getVector().size(); i++) {
            // [0, 1]
            double rabbitSize = 0.15;
            Rabbit rab = Singleton.getVector().get(i);
            BufferedImage bufferedImage = rab instanceof Ordinary ? (Ordinary.getImg()) : (Albino.getImg());
            offScreenGraphics.drawImage(bufferedImage, rab.getX() * 2,rab.getY() * 2, (int)(rabbitSize *w), (int)(rabbitSize *h), null);
        }

        //вывод изображение на фрейм
        jPanelImage.getGraphics().drawImage(offScreenImage,0,0,w/2,h/2,null); //делю на 2, чтобы вернуться к размерам фрейма
    }

    private void createPanelUI(Container container) {
        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        container.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5,5,5,5);
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.1;
        constraints.weighty = 1;
        container.add(jPanelControl, constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.9;
        container.add(jPanelImage, constraints);


        jButtonStop.setEnabled(false);
        JCheckBox jCheckBoxShowInf = new JCheckBox("Показывать информацию");

        ButtonGroup group = new ButtonGroup();
        jRadioButtonNoShowTime.setSelected(true);
        group.add(jRadioButtonShowTime);
        group.add(jRadioButtonNoShowTime);

        String[] item = new String[11];
        for (int i = 0; i <= 10; i++) {
            item[i] = Integer.toString(i*10);
            jComboBox.addItem(Integer.toString(i*10));
        }
        jComboBox.setSelectedIndex(P);

        for (int i = 0; i < 11; i++) {
            jComboBoxOrdinaryPriority.addItem(i);
            jComboBoxAlbinoPriority.addItem(i);
        }
        jComboBoxOrdinaryPriority.setSelectedIndex(PO);
        jComboBoxAlbinoPriority.setSelectedIndex(PA);

        jList.setListData(item);
        jList.setSelectedIndex(K);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(jList);
        jList.setLayoutOrientation(JList.VERTICAL);
        JPanel jPanelList = new JPanel(new BorderLayout());
        jPanelList.add(jScrollPane);


        jTextFieldNOrdinary.setText(Integer.toString(NO));
        jTextFieldNAlbino.setText(Integer.toString(NA));
        jTextFieldLiveTimeOrdinary.setText(Integer.toString(LTO));
        jTextFieldLiveTimeAlbino.setText(Integer.toString(LTA));

        jPanelControl.setLayout(new GridLayout(25,1,5,5));
        jPanelControl.add(jButtonStart);
        jPanelControl.add(jButtonStop);
        jPanelControl.add(jRadioButtonShowTime);
        jPanelControl.add(jRadioButtonNoShowTime);
        jPanelControl.add(jCheckBoxShowInf);
        jPanelControl.add(jLabelP);
        jPanelControl.add(jComboBox);
        jPanelControl.add(jLabelK);
        jPanelControl.add(jPanelList);
        jPanelControl.add(jLabelNOrdinary);
        jPanelControl.add(jTextFieldNOrdinary);
        jPanelControl.add(jLabelNAlbino);
        jPanelControl.add(jTextFieldNAlbino);
        jPanelControl.add(jLabelBirthTimeOrdinary);
        jPanelControl.add(jTextFieldLiveTimeOrdinary);
        jPanelControl.add(jLabelBirthTimeAlbino);
        jPanelControl.add(jTextFieldLiveTimeAlbino);
        jPanelControl.add(jButtonObjects);
        jPanelControl.add(jButtonOrdinaryAI);
        jPanelControl.add(jButtonAlbinoAI);
        jPanelControl.add(jLabelOrdinaryPriority);
        jPanelControl.add(jComboBoxOrdinaryPriority);
        jPanelControl.add(jLabelAlbinoPriority);
        jPanelControl.add(jComboBoxAlbinoPriority);
        jPanelControl.add(jLabel);

        jButtonStart.addActionListener(e -> ActionStart());

        jButtonStop.addActionListener(e -> ActionStop());

        jButtonObjects.addActionListener(e -> ActionShowObjects());

        jCheckBoxShowInf.addItemListener(e -> {
            timer.cancel();
            flag_is_work = false;
            jTextArea.setText("Кроликов всего " + Rabbit.getAllQuantity() +
                    "\nОбычных " + Ordinary.getOrdinaryQuantity() +
                    "\nАльбиносов " + Albino.getAlbinoQuantity() +
                    "\n\nВремени прошло " + cumulativePeriod + " миллисекунд");
            jDialog.setVisible(true);
        });

        jRadioButtonShowTime.addActionListener(e -> RadioButtonBoolean = true);

        jRadioButtonNoShowTime.addActionListener(e -> {
            RadioButtonBoolean = false;
            jLabel.setVisible(false);
        });

        jComboBox.addActionListener(e -> Habitat.POrdinary = (double)Integer.parseInt((String) Objects.requireNonNull(jComboBox.getSelectedItem())) / 100);

        jList.addListSelectionListener(e -> Habitat.KAlbino =  (double) Integer.parseInt((String)jList.getSelectedValue()) / 100);

        jButtonCancel.addActionListener(e -> {
            jDialog.setVisible(false);
            ActionStart();
        });

        jButtonOk.addActionListener(e -> {
            jDialog.setVisible(false);
            cumulativePeriod = 0;
            Singleton.getVector().clear();
            Singleton.getHashMap().clear();
            Singleton.getTreeSetID().clear();
            jButtonStop.setEnabled(false);
            jButtonStart.setEnabled(true);
        });

        jButtonAlbinoAI.addActionListener(e -> {
            albinoAI.setPriority(jComboBoxAlbinoPriority.getSelectedIndex());
            albinoAI.isActive = !albinoAI.isActive;
        });

        jButtonOrdinaryAI.addActionListener(e -> {
            ordinaryAI.setPriority(jComboBoxOrdinaryPriority.getSelectedIndex());
            ordinaryAI.isAlive = !ordinaryAI.isAlive;
        });
    }

    private void Serialization() {
        Serializator serializator = new Serializator();
        serializator.Serialization();
    }

    private void Deserialization() {
        if (flag_is_work) ActionStop();
        Serializator serializator = new Serializator();
        serializator.Deserialization();
        ActionStart();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu FileMenu = new JMenu("Файл");

            JMenuItem SaveItem = new JMenuItem("Сохранить");
            FileMenu.add(SaveItem);
            SaveItem.addActionListener(e -> Serialization());

            JMenuItem LoadItem = new JMenuItem("Загрузить");
            FileMenu.add(LoadItem);
            LoadItem.addActionListener(e -> Deserialization());

        JMenu SimulationMenu = new JMenu("Симуляция");

            JMenuItem StartItem = new JMenuItem("Старт");
            SimulationMenu.add(StartItem);
            StartItem.addActionListener(e -> ActionStart());

            JMenuItem StopItem = new JMenuItem("Стоп");
            SimulationMenu.add(StopItem);
            StopItem.addActionListener(e -> ActionStop());

            JMenuItem exitItem = new JMenuItem("Выход");
            SimulationMenu.add(exitItem);
            exitItem.addActionListener(e -> System.exit(0));

        JMenu SettingsMenu = new JMenu("Настройки");

            JMenu ShowTimeMenu = new JMenu("Показывать время");
            SettingsMenu.add(ShowTimeMenu);

                JMenuItem YesItem = new JMenuItem("Да");
                ShowTimeMenu.add(YesItem);
                YesItem.addActionListener(e -> RadioButtonBoolean = true);

                JMenuItem NoItem = new JMenuItem("Нет");
                ShowTimeMenu.add(NoItem);
                NoItem.addActionListener(e -> {
                    RadioButtonBoolean = false;
                    jLabel.setVisible(false);
                });

            JMenuItem ShowInfItem = new JMenuItem("Информация о симуляции");
            SettingsMenu.add(ShowInfItem);
            ShowInfItem.addActionListener(e -> {
                timer.cancel();
                flag_is_work = false;
                jTextArea.setText("Кроликов всего " + Rabbit.getAllQuantity() +
                        "\nОбычных " + Ordinary.getOrdinaryQuantity() +
                        "\nАльбиносов " + Albino.getAlbinoQuantity() +
                        "\n\nВремени прошло " + cumulativePeriod + " миллисекунд");
                jDialog.setVisible(true);
            });

        JMenu jMenuDB = new JMenu("База данных");
            JMenu jMenuSave = new JMenu("Сохранить");
            jMenuDB.add(jMenuSave);
                JMenuItem jMenuItemSaveAll = new JMenuItem("Всех");
                jMenuSave.add(jMenuItemSaveAll);
                jMenuItemSaveAll.addActionListener(e -> new DBSaver().SaveAll());

                JMenuItem jMenuItemSaveAlbino = new JMenuItem("Альбиносов");
                jMenuSave.add(jMenuItemSaveAlbino);
                jMenuItemSaveAlbino.addActionListener(e -> new DBSaver().SaveAlbino());

                JMenuItem jMenuItemSaveOrdinary = new JMenuItem("Обыкновенных");
                jMenuSave.add(jMenuItemSaveOrdinary);
                jMenuItemSaveOrdinary.addActionListener(e -> new DBSaver().SaveOrdinary());

            JMenu jMenuLoad = new JMenu("Загрузить");
            jMenuDB.add(jMenuLoad);
                JMenuItem jMenuItemLoadAll = new JMenuItem("Всех");
                jMenuLoad.add(jMenuItemLoadAll);
                jMenuItemLoadAll.addActionListener(e -> new DBSaver().LoadAll());

                JMenuItem jMenuItemLoadAlbino = new JMenuItem("Альбиносов");
                jMenuLoad.add(jMenuItemLoadAlbino);
                jMenuItemLoadAlbino.addActionListener(e -> new DBSaver().LoadAlbino());

                JMenuItem jMenuItemLoadOrdinary = new JMenuItem("Обыкновенных");
                jMenuLoad.add(jMenuItemLoadOrdinary);
                jMenuItemLoadOrdinary.addActionListener(e -> new DBSaver().LoadOrdinary());

        JMenuItem StartConsole = new JMenuItem("Консоль");
        StartConsole.addActionListener(e -> jDialogConsole.setVisible(true));

        JMenuItem ClientItem = new JMenuItem("Клиент");
        ClientItem.addActionListener(e -> jDialogClient.setVisible(true));

        menuBar.add(FileMenu);
        menuBar.add(SimulationMenu);
        menuBar.add(SettingsMenu);
        menuBar.add(jMenuDB);
        menuBar.add(StartConsole);
        menuBar.add(ClientItem);

        setJMenuBar(menuBar);
    }
}
