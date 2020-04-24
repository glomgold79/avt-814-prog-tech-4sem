import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class GUI extends JFrame {

    BufferedImage offScreenImage;
    boolean flag_is_work = false;
    private Timer timer;
    private static BufferedImage BackgroundImg;
    private long cumulativePeriod = 0;
    private final long period = 1;
    private AlbinoAI albinoAI = new AlbinoAI();
    private boolean isAlbinoAI = false;
    private OrdinaryAI ordinaryAI = new OrdinaryAI();
    private boolean isOrdinaryAI = false;

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
    JTextArea jTextArea = new JTextArea();
    JTextArea jTextAreaObjects = new JTextArea();
    JButton jButtonOk = new JButton("Окей"), jButtonCancel = new JButton("Отмена");
    JComboBox<String> jComboBox = new JComboBox<>();
    JComboBox<Integer> jComboBoxOrdinaryPriority = new JComboBox<>();
    JComboBox<Integer> jComboBoxAlbinoPriority = new JComboBox<>();
    JList<String> jList = new JList<>();
    JTextField jTextFieldNOrdinary = new JTextField();
    JTextField jTextFieldNAlbino = new JTextField();
    JTextField jTextFieldLiveTimeOrdinary = new JTextField();
    JTextField jTextFieldLiveTimeAlbino = new JTextField();


    Boolean RadioButtonBoolean = false;

    public GUI(int Width, int Height) {
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

            jTextFieldNOrdinary.setText("10");
            jTextFieldNAlbino.setText("20");
            jTextFieldLiveTimeOrdinary.setText("100");
            jTextFieldLiveTimeAlbino.setText("80");

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
        for (Rabbit rab : Singleton.getVector()) {
            // [0, 1]
            double rabbitSize = 0.15;
            offScreenGraphics.drawImage(rab.getImg(), rab.getX() * 2,rab.getY() * 2, (int)(rabbitSize *w), (int)(rabbitSize *h), null);
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
        JRadioButton jRadioButtonShowTime = new JRadioButton("Показать время симуляции"),
                     jRadioButtonNoShowTime = new JRadioButton("Не показывать время симуляции");
        jRadioButtonNoShowTime.setSelected(true);
        group.add(jRadioButtonShowTime);
        group.add(jRadioButtonNoShowTime);

        String[] item = new String[11];
        for (int i = 0; i <= 10; i++) {
            item[i] = Integer.toString(i*10);
            jComboBox.addItem(Integer.toString(i*10));
        }
        jComboBox.setSelectedIndex(5);

        for (int i = 0; i < 11; i++) {
            jComboBoxOrdinaryPriority.addItem(i);
            jComboBoxAlbinoPriority.addItem(i);
        }
        jComboBoxOrdinaryPriority.setSelectedIndex(6);
        jComboBoxAlbinoPriority.setSelectedIndex(2);

        jList.setListData(item);
        jList.setSelectedIndex(7);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(jList);
        jList.setLayoutOrientation(JList.VERTICAL);
        JPanel jPanelList = new JPanel(new BorderLayout());
        jPanelList.add(jScrollPane);


        jTextFieldNOrdinary.setText("10");
        jTextFieldNAlbino.setText("20");
        jTextFieldLiveTimeOrdinary.setText("100");
        jTextFieldLiveTimeAlbino.setText("80");

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

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

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

        JMenu SettingsMenu = new JMenu("Настройки вывода");

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

        menuBar.add(SimulationMenu);
        menuBar.add(SettingsMenu);

        setJMenuBar(menuBar);
    }
}
