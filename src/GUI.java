import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class GUI extends JFrame {

    BufferedImage offScreenImage;
    boolean flag_is_work = false;
    private Timer timer;
    private static BufferedImage BackgroundImg;
    private final double RabbitSize = 0.15; // [0, 1]
    private long cumulativePeriod = 0;
    private final long period = 1;

    JPanel jPanelImage = new JPanel(), jPanelControl = new JPanel();
    JButton jButtonStart = new JButton("Старт"), jButtonStop = new JButton("Стоп");
    JLabel jLabel = new JLabel();
    JDialog jDialog = new JDialog(this,"Информация о симуляции", true);
    JTextArea jTextArea = new JTextArea();
    JButton jButtonOk = new JButton("Окей"), jButtonCancel = new JButton("Отмена");
    JComboBox jComboBox = new JComboBox();
    JList jList = new JList();
    JTextField jTextFieldNOrdinary = new JTextField();
    JTextField jTextFieldNAlbino = new JTextField();

    Boolean RadioButtonBoolean = false;

    public GUI(int Width, int Height){
        jDialog.setSize(200,350);
        jDialog.setLocation(350,200);
        jDialog.setLayout(new GridLayout(3,1, 15,15));
        jTextArea.setEditable(false);
        jDialog.add(jTextArea);
        jDialog.add(jButtonOk);
        jDialog.add(jButtonCancel);

        try {
            BackgroundImg = ImageIO.read(new File("./Images/Field.jpg"));
        } catch (IOException Ex) {
            System.out.println(Ex);
        }
        setTitle("Молнер Василий АВТ-814 Вариант №4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(Width, Height));
        setFocusable(true);
        setLocation(300,230);
        creatMenuBar();
        creatPanelUI(getContentPane());
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
        Habitat.POrdinary = (double) Integer.parseInt((String) jComboBox.getSelectedItem()) / 100;
        Habitat.KAlbino = (double) Integer.parseInt((String) jList.getSelectedValue()) / 100;
        try {
            Habitat.NOrdinary = Integer.parseInt(jTextFieldNOrdinary.getText());
            Habitat.NAlbino = Integer.parseInt(jTextFieldNAlbino.getText());
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

            jButtonExOk.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jDialogEx.setVisible(false);
                }
            });

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
        Singleton.getLink().clear();
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
        String string = new String(
                "Время " + cumulativePeriod + " миллисекунд");
        jLabel.setText(string);
        add(jLabel);
        setVisible(true);
    }

    public void Render() {
        //Двойная буфферизация. Рисую в BufferedImage, и только готовое вывожу в фрейм (устраняю мерцание)
        int w = jPanelImage.getWidth()*2, h = jPanelImage.getHeight()*2; //умножаю на 2, потому что падает качество
        offScreenImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics offScreenGraphics= offScreenImage.getGraphics();

        offScreenGraphics.drawImage(BackgroundImg, 0,0, w, h,null);
        for (Rabbit rab : Singleton.getLink()) {
            offScreenGraphics.drawImage(rab.getImg(), (int)(rab.getX()*w),(int)(rab.getY()*h), (int)(RabbitSize*w), (int)(RabbitSize*h), null);
        }

        //вывод изображение на фрейм
        jPanelImage.getGraphics().drawImage(offScreenImage,0,0,w/2,h/2,null); //делю на 2, чтобы вернуться к размерам фрейма
    }

    private void creatPanelUI(Container container) {
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
        group.add(jRadioButtonShowTime);
        group.add(jRadioButtonNoShowTime);

        String item[] = new String[11];
        for (int i = 0; i <= 10; i++) {
            item[i] = Integer.toString(i*10);
            jComboBox.addItem(Integer.toString(i*10));
        }
        jComboBox.setSelectedIndex(5);

        jList.setListData(item);
        jList.setSelectedIndex(7);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(jList);
        jList.setLayoutOrientation(JList.VERTICAL);
        JPanel jPanelList = new JPanel(new BorderLayout());
        jPanelList.add(jScrollPane);


        jTextFieldNOrdinary.setText("10");
        jTextFieldNAlbino.setText("20");

        jPanelControl.setLayout(new GridLayout(10,1,5,5));
        jPanelControl.add(jButtonStart);
        jPanelControl.add(jButtonStop);
        jPanelControl.add(jRadioButtonShowTime);
        jPanelControl.add(jRadioButtonNoShowTime);
        jPanelControl.add(jCheckBoxShowInf);
        jPanelControl.add(jComboBox);
        jPanelControl.add(jPanelList);
        jPanelControl.add(jTextFieldNOrdinary);
        jPanelControl.add(jTextFieldNAlbino);
        jPanelControl.add(jLabel);

        jButtonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionStart();
            }
        });

        jButtonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionStop();
            }
        });

        jCheckBoxShowInf.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                timer.cancel();
                flag_is_work = false;
                jTextArea.setText("Кроликов всего " + Rabbit.getAllQuantity() +
                        "\nОбычных " + Ordinary.getOrdinaryQuantity() +
                        "\nАльбиносов " + Albino.getAlbinoQuantity() +
                        "\n\nВремени прошло " + cumulativePeriod + " миллисекунд");
                jDialog.setVisible(true);
            }
        });

        jRadioButtonShowTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RadioButtonBoolean = true;
            }
        });

        jRadioButtonNoShowTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RadioButtonBoolean = false;
                jLabel.setVisible(false);
            }
        });

        jComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Habitat.POrdinary = (double)Integer.parseInt((String)jComboBox.getSelectedItem()) / 100;
            }
        });

        jList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Habitat.KAlbino =  (double) Integer.parseInt((String)jList.getSelectedValue()) / 100;
            }
        });

        jButtonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jDialog.setVisible(false);
                ActionStart();
            }
        });

        jButtonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jDialog.setVisible(false);
                cumulativePeriod = 0;
                Singleton.getLink().clear();
                jButtonStop.setEnabled(false);
                jButtonStart.setEnabled(true);
            }
        });
    }

    private void creatMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu SimulationMenu = new JMenu("Симуляция");

            JMenuItem StartItem = new JMenuItem("Старт");
            SimulationMenu.add(StartItem);
            StartItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ActionStart();
                }
            });

            JMenuItem StopItem = new JMenuItem("Стоп");
            SimulationMenu.add(StopItem);
            StopItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ActionStop();
                }
            });

            JMenuItem exitItem = new JMenuItem("Выход");
            SimulationMenu.add(exitItem);
            exitItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

        JMenu SettingsMenu = new JMenu("Настройки вывода");

            JMenu ShowTimeMenu = new JMenu("Показывать время");
            SettingsMenu.add(ShowTimeMenu);

                JMenuItem YesItem = new JMenuItem("Да");
                ShowTimeMenu.add(YesItem);
                YesItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        RadioButtonBoolean = true;
                    }
                });

                JMenuItem NoItem = new JMenuItem("Нет");
                ShowTimeMenu.add(NoItem);
                NoItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        RadioButtonBoolean = false;
                        jLabel.setVisible(false);
                    }
                });

            JMenuItem ShowInfItem = new JMenuItem("Информация о симуляции");
            SettingsMenu.add(ShowInfItem);
            ShowInfItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    timer.cancel();
                    flag_is_work = false;
                    jTextArea.setText("Кроликов всего " + Rabbit.getAllQuantity() +
                            "\nОбычных " + Ordinary.getOrdinaryQuantity() +
                            "\nАльбиносов " + Albino.getAlbinoQuantity() +
                            "\n\nВремени прошло " + cumulativePeriod + " миллисекунд");
                    jDialog.setVisible(true);
                }
            });

        menuBar.add(SimulationMenu);
        menuBar.add(SettingsMenu);

        setJMenuBar(menuBar);
    }

}
