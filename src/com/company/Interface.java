package com.company;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

public class Interface {

    int width, height;
    AtomicLong sumTime;
    long time;

    boolean VisibleTime, VisibleStat, VisibleModalWindow, Click;

    JFrame frame;
    JPanel Scene, settingsScene;

    JLabel TimeLabel;
    JLabel statistics;

    Drawing DP;

    Habitat Window;

    public PipedWriter PW;
    PipedReader PR;
    Console console;

    Color color = new Color(240, 240, 240);
    Color color2 = new Color(100,100,100);
    JDialog modalWind = new JDialog(new JFrame());
    JButton Start = new JButton("Start");
    JButton Stop = new JButton("Stop");
    JButton Ok = new JButton("Ok");
    TextArea Text = new TextArea();
    JButton Cancel = new JButton("Cancel");
    GridBagConstraints settGrid = new GridBagConstraints();
    ButtonGroup group = new ButtonGroup();
    JRadioButton Visib, notVisib;
    JCheckBox Check = new JCheckBox("Statistic Visible");
    DefaultComboBoxModel<String> Dmodel = new DefaultComboBoxModel<String>();
    DefaultComboBoxModel<String> Dmodel2 = new DefaultComboBoxModel<String>();
    JMenuBar MainMenu = new JMenuBar();
    JMenu menu = new JMenu("Menu");
    JRadioButton Mvis, MnotVis;
    JButton MStart = new JButton("Start");
    JButton MStop = new JButton("Stop");
    JCheckBox MCheck = new JCheckBox("Statistic Visible");
    JCheckBox console1 = new JCheckBox("Console");
    JComboBox<String> Comb = new JComboBox<String>(Dmodel);
    JComboBox<String> Comb2 = new JComboBox<String>(Dmodel2);
    ButtonGroup MGroup = new ButtonGroup();
    JCheckBox Check2 = new JCheckBox("Allow input");
    JTextArea forPerson, forLaw, timelivePerson, timelivelaw;
    JPanel K1 = new JPanel();
    JPanel K2 = new JPanel(new GridLayout(4,2,3,2));
    JPanel timeBlock = new JPanel();
    JPanel SS = new JPanel();
    JButton RObj = new JButton("Current objects");
    JPanel ButtonsStream = new JPanel(new GridLayout(3,2));
    JButton PersonButton = new JButton("AI P start");
    JButton LawButton = new JButton("AI L start");
    JComboBox<String> combPerson;
    JComboBox<String> combLaw;
    DefaultComboBoxModel<String> set1 = new DefaultComboBoxModel<>();
    DefaultComboBoxModel<String> set2 = new DefaultComboBoxModel<>();
    JDialog ExeptionWind = new JDialog();
    JLabel L1 = new JLabel("please input correct value");
    JDialog ConsolWind = new JDialog(frame,"Console",false);
    JTextArea cons = new JTextArea();
    JButton Save = new JButton("Save");
    JButton Load = new JButton("Load");
    SaveLoad saver = new SaveLoad();
    SaveObj save = new SaveObj();

    class MyTask extends TimerTask {

        private long time;
        private Habitat Window;
        float koef = (float)MySingleton.pack.timeKoeff;

        @Override
        public void run(){
            time += 1;

            Window.Update(time);
            DP.draw(DP.getGraphics());

            TimeLabel.setText("Time - " + time*koef + " sec");

            if(time%(1/MySingleton.pack.timeKoeff) == 0){
                sumTime.set(sumTime.get()+1);
            }
        }

        MyTask(Habitat Window, long time){
            this.Window = Window;
            this.time = time;
        }
    }

    class SaveLoad{

        public void Save(){
            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run(){
                    File file = new File("Config.txt");
                    try{
                        FileWriter writer = new FileWriter(file);
                        writer.write(Long.toString(time)+" ");
                        writer.write(Boolean.toString(Visib.isSelected())+" ");
                        writer.write(Boolean.toString(notVisib.isSelected())+" ");
                        writer.write(Boolean.toString(Check.isSelected())+" ");
                        writer.write(Integer.toString(MySingleton.pack.numbersPerson)+" ");
                        writer.write(Integer.toString(MySingleton.pack.numdersLaw)+" ");
                        writer.write(Boolean.toString(MySingleton.pack.runTime)+" ");

                        writer.flush();
                        writer.close();
                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    void Event(int numEvent){
        switch (numEvent){
            case 66:
                if(MySingleton.pack.runTime == true){
                    MySingleton.pack.timer.cancel();
                    MySingleton.pack.runTime = false;
                }
                if(VisibleStat == true){
                    statistics.setVisible(false);
                    VisibleStat = false;
                }

                if(VisibleStat == true){
                    VisibleStat = false;
                    statistics.setVisible(false);
                }
                frame.repaint();
                MySingleton.pack.timer = new Timer();
                MyTask task = new MyTask(Window,time);
                MySingleton.pack.runTime = true;
                MySingleton.pack.timer.schedule(task,0,(long)(MySingleton.pack.timeKoeff*1000));
                break;

            case 84:
                if(VisibleTime == false) {
                    if (VisibleStat == false) {
                        TimeLabel.setVisible(true);
                        VisibleTime = true;
                    }
                }else{
                    TimeLabel.setVisible(false);
                    VisibleTime = false;
                }
                break;

            case 69:
                if(VisibleStat == false) {
                    if (MySingleton.pack.runTime == true) {
                        MySingleton.pack.timer.cancel();
                        MySingleton.pack.runTime = false;
                    }
                    if (VisibleTime == true) {
                        VisibleTime = false;
                        TimeLabel.setVisible(false);
                    }
                    VisibleStat = true;
                    frame.repaint();
                    statistics.setText("<html>Time - " + sumTime.get() + " sec" + "<br>Total numbers - " +
                            MySingleton.pack.Array.size() +
                            "<br>Legal Persons - " + MySingleton.pack.numdersLaw +
                            "<br>Individual Persons - " + MySingleton.pack.numbersPerson + "<html>");
                    sumTime.set(0);
                    time = 0;
                    statistics.setVisible(true);
                    MySingleton.pack.clear();
                }
                break;

            default: break;
        }
    }

    void Frame() {

        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        modalWind.setSize(350,200);

        modalWind.setLayout(new GridBagLayout());
        Text.setSize(350,150);
        Text.setEnabled(false);
        Text.setPreferredSize(new Dimension(200,150));

        modalWind.setMinimumSize(modalWind.getSize());
        modalWind.setMaximumSize(modalWind.getSize());
        modalWind.add(Ok);
        modalWind.add(Cancel);
        settGrid.fill = GridBagConstraints.HORIZONTAL;
        settGrid.gridwidth = GridBagConstraints.REMAINDER;
        settGrid.weightx = 1.0f;
        modalWind.setLocation(400,350);
        modalWind.setVisible(false);

        Scene.setSize(width, height);
        DP.setBackground(color);
        DP.setDoubleBuffered(true);
        Scene.setBackground(color2);
        Scene.add(DP, BorderLayout.EAST);
        Stop.setEnabled(false);
        Visib = new JRadioButton("Visible time");
        Visib.setSelected(false);
        group.add(Visib);
        notVisib = new JRadioButton("not visible time");
        notVisib.setSelected(true);
        group.add(notVisib);

        Check.setSelected(false);

        String[] array = new String[10];
        for(int i = 0; i < 10; i++){
            array[i] = (String)(((i+1)*10)+"%");
        }
        for(int i = 0; i < 10; i ++){
            Dmodel.addElement(array[i]);
            Dmodel2.addElement(array[i]);
        }

        MainMenu.add(menu);

        Mvis = new JRadioButton("Visible time");
        Mvis.setSelected(false);
        MnotVis = new JRadioButton("not visible time");
        MnotVis.setSelected(true);
        MGroup.add(Mvis);
        MGroup.add(MnotVis);
        MStop.setEnabled(false);
        MCheck.setSelected(false);

        console1.setSelected(false);

        menu.add(Mvis);
        menu.add(MnotVis);
        menu.add(MCheck);
        menu.add(Save);
        menu.add(Load);
        menu.add(console1);
        menu.add(MStart);
        menu.add(MStop);


        K1.add(new JLabel("Propability generation person"));
        K1.add(Comb);
        K1.add(new JLabel("Propability generation law"));
        K1.add(Comb2);


        forPerson = new JTextArea();
        forLaw = new JTextArea();
        timelivePerson = new JTextArea();
        timelivelaw = new JTextArea();
        forPerson.setPreferredSize(new Dimension(100,18));
        forLaw.setPreferredSize(new Dimension(100,18));
        timelivePerson.setPreferredSize(new Dimension(100,18));
        timelivelaw.setPreferredSize(new Dimension(100,18));

        K2.add(new JLabel("Period generation person"));
        K2.add(forPerson);
        K2.add(new JLabel("Period generation law"));
        K2.add(forLaw);
        K2.add(new JLabel("Time live Person"));
        K2.add(timelivePerson);
        K2.add(new JLabel("Time live Law"));
        K2.add(timelivelaw);

        timeBlock.add(Visib);
        timeBlock.add(notVisib);

        SS.add(Start);
        SS.add(Stop);
        SS.add(RObj);

        set1.addElement("Max");
        set1.addElement("Med");
        set1.addElement("Min");

        set2.addElement("Max");
        set2.addElement("Med");
        set2.addElement("Min");

        combPerson = new JComboBox<>(set1);
        combLaw = new JComboBox<>(set2);
        combPerson.setSelectedIndex(1);
        combLaw.setSelectedIndex(1);
        ButtonsStream.add(PersonButton);
        ButtonsStream.add(LawButton);
        ButtonsStream.add(new JLabel("Person priority"));
        ButtonsStream.add(new JLabel("Law priority"));
        ButtonsStream.add(combPerson);
        ButtonsStream.add(combLaw);

        settingsScene.setBackground(color);
        settingsScene.setLayout(new GridLayout(7,1));
        settingsScene.add(timeBlock);
        settingsScene.add(Check);
        settingsScene.add(K1);
        settingsScene.add(Check2);
        settingsScene.add(K2);
        settingsScene.add(ButtonsStream);
        settingsScene.add(SS);

        Comb.setSelectedIndex(6);
        Comb2.setSelectedIndex(2);

        frame.add(settingsScene, BorderLayout.CENTER);
        frame.setJMenuBar(MainMenu);

        Start.setFocusable(false);
        Stop.setFocusable(false);
        Visib.setFocusable(false);
        notVisib.setFocusable(false);
        Check.setFocusable(false);
        Comb.setFocusable(false);
        Comb2.setFocusable(false);
        Check2.setFocusable(false);
        K2.setFocusable(false);
        forPerson.setFocusable(false);
        forLaw.setFocusable(false);
        timelivelaw.setFocusable(false);
        timelivePerson.setFocusable(false);
        RObj.setFocusable(false);
        PersonButton.setFocusable(false);
        LawButton.setFocusable(false);
        combPerson.setFocusable(false);
        combLaw.setFocusable(false);
        console1.setFocusable(false);

        frame.add(Scene, BorderLayout.EAST);

        TimeLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        DP.add(TimeLabel);
        TimeLabel.setVisible(VisibleTime);

        statistics.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        statistics.setBackground(DP.getBackground());
        DP.add(statistics);
        statistics.setVisible(VisibleStat);

        ExeptionWind.setSize(400,200);
        L1.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        ExeptionWind.add(L1, BorderLayout.CENTER);
        ExeptionWind.setModal(true);
        ExeptionWind.setVisible(false);

        ConsolWind.setSize(350,500);
        ConsolWind.setVisible(false);
        ConsolWind.setLocation(0,100);

        ConsolWind.add(cons,BorderLayout.CENTER);

        Window.RunAI();
        saver.Save();

        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event){
                if(event.getKeyCode() == 84){
                    if(VisibleTime == true){
                        Visib.setSelected(false);
                        notVisib.setSelected(true);
                        return;
                    }else{
                        notVisib.setSelected(false);
                        Visib.setSelected(true);
                        return;
                    }
                }
                if(event.getKeyCode() == 69){
                    if(VisibleTime == true){
                        Visib.setSelected(false);
                        //Visib.setEnabled(false);
                        notVisib.setSelected(true);
                        VisibleTime = true;
                    }/*else{
                        notVisib.setSelected(false);
                        Visib.setSelected(true);
                        VisibleTime = false;
                    }*/
                    Start.setEnabled(true);
                    Stop.setEnabled(false);
                }
                if(event.getKeyCode() == 66){
                    Start.setEnabled(false);
                    Stop.setEnabled(true);
                }
                Event(event.getKeyCode()); }});
        Start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(MySingleton.pack.runTime == false){
                    Visib.setEnabled(true);
                    Mvis.setEnabled(true);
                    Event(66);
                    Start.setEnabled(false);
                    MStart.setEnabled(false);
                    Stop.setEnabled(true);
                    MStop.setEnabled(true);
                }}});
        Stop.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               notVisib.setSelected(true);
               VisibleTime = true;
               Visib.setEnabled(false);
               Mvis.setEnabled(false);

               if(VisibleModalWindow == true){
                   modalWind.setVisible(true);
               }
               if(VisibleModalWindow == true){

                   Text.setText("Time - " + sumTime.get() + "\n" +
                           "Total numbers - " + (MySingleton.pack.numbersPerson+MySingleton.pack.numdersLaw)+ "\n" +
                           "law persons - " + MySingleton.pack.numdersLaw + "\n" +
                           "Individual persons - " + MySingleton.pack.numbersPerson);
                   modalWind.add(Text);
                   modalWind.setVisible(true);
                   modalWind.setModal(true);
                   MySingleton.pack.timer.cancel();
                   MySingleton.pack.runTime = false;

                   Ok.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                           Visib.setEnabled(false);
                           //modalWind.setModal(false);
                           Mvis.setEnabled(false);
                           Event(69);
                           modalWind.setVisible(false);
                           Start.setEnabled(true);
                           Stop.setEnabled(false);
                           MStart.setEnabled(true);
                           MStop.setEnabled(false);
                       }
                   });
                   Cancel.addActionListener(new ActionListener() {
                       @Override
                       public void actionPerformed(ActionEvent e) {
                           Event(66);
                           modalWind.setVisible(false);
                           //modalWind.setModal(false);
                           Visib.setEnabled(true);
                           Mvis.setEnabled(true);
                       }
                   });
                   return;
               }else{
               Event(69);
               Stop.setEnabled(false);
               MStop.setEnabled(false);
               Start.setEnabled(true);
               MStart.setEnabled(true);
               }
           }
        });
        Visib.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(VisibleTime == false){
                Event(84);
                Mvis.setSelected(true);
                }
            }
        });
        notVisib.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(VisibleTime == true){
                Event(84);
                MnotVis.setSelected(true);
                }
            }
        });
        Check.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(VisibleModalWindow == false) {
                    //modalWind.setVisible(true);
                    VisibleModalWindow = true;
                    if(Click == true){
                        MCheck.setSelected(true);
                        Click = false;
                    }else{
                        Click = true;
                        MCheck.setSelected(true);
                    }
                }else{
                    //modalWind.setVisible(false);
                    VisibleModalWindow = false;
                    if(Click == true){
                        MCheck.setSelected(false);
                        Click = false;
                    }else{
                        Click = true;
                        MCheck.setSelected(false);
                    }
                }
            }
        });
        Comb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String aa = new String((String) Comb.getSelectedItem());
                String[] m = aa.split("");
                aa = new String();
                for(int i = 0; i < m.length - 1; i++){
                    aa += m[i].toString();
                }
                Window.setProbabilityPerson(Double.parseDouble(aa)/100);
            }
        });
        Comb2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String aa = new String((String) Comb2.getSelectedItem());
                String[] m = aa.split("");
                aa = new String();
                for(int i = 0; i < m.length - 1; i++){
                    aa += m[i].toString();
                }
                Window.setProbabilityLaw(Double.parseDouble(aa)/100);
            }
        });
        Check2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(Check2.isSelected()){
                   forPerson.setFocusable(true);
                   forLaw.setFocusable(true);
                   timelivePerson.setFocusable(true);
                   timelivelaw.setFocusable(true);
                }else{
                    forPerson.setFocusable(false);
                    forLaw.setFocusable(false);
                    timelivePerson.setFocusable(false);
                    timelivelaw.setFocusable(false);
                    Check2.setFocusable(false);
                    frame.setFocusable(true);
                }
            }
        });
        forPerson.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try{
                    int t = Integer.parseInt(forPerson.getText());
                    Window.setPeriodPerson(t);
                }catch(NumberFormatException error) {
                    ExeptionWind.setVisible(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try{
                    int t = Integer.parseInt(forPerson.getText());
                    Window.setPeriodPerson(t);
                }catch(NumberFormatException error) {
                    ExeptionWind.setVisible(true);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        forLaw.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try{
                    int t = Integer.parseInt(forPerson.getText());
                    Window.setPeriodLaw(t);
                }catch(NumberFormatException error) {
                    ExeptionWind.setVisible(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try{
                    int t = Integer.parseInt(forPerson.getText());
                    Window.setPeriodLaw(t);
                }catch(NumberFormatException error) {
                    ExeptionWind.setVisible(true);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        timelivePerson.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try{
                    int t = Integer.parseInt(timelivePerson.getText());
                    Window.setTimelivePerson(t);
                }catch(NumberFormatException error) {
                    ExeptionWind.setVisible(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try{
                    int t = Integer.parseInt(timelivePerson.getText());
                    Window.setTimelivePerson(t);
                }catch(NumberFormatException error) {
                    ExeptionWind.setVisible(true);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        timelivelaw.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try{
                    int t = Integer.parseInt(timelivelaw.getText());
                    Window.setTimeliveLaw(t);
                }catch(NumberFormatException error) {
                    ExeptionWind.setVisible(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try{
                    int t = Integer.parseInt(timelivelaw.getText());
                    Window.setTimeliveLaw(t);
                }catch(NumberFormatException error) {
                    ExeptionWind.setVisible(true);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        RObj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog newModal = new JDialog();
                newModal.setSize(400,200);
                JLabel L1 = new JLabel("All objects");
                JTextArea T1 = new JTextArea();
                T1.setText(MySingleton.pack.hash.toString());
                T1.setEnabled(false);
                L1.setFont(new Font("Times New Roman", Font.PLAIN, 15));
                newModal.add(L1, BorderLayout.NORTH);
                newModal.add(T1,BorderLayout.CENTER);
                newModal.setModal(true);
                newModal.setVisible(true);
            }
        });
        PersonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(MySingleton.pack.runPersonTime == false){
                    MySingleton.pack.runPersonTime = true;
                    synchronized (MySingleton.pack.AIPerson){
                        MySingleton.pack.AIPerson.notify();
                    }
                    PersonButton.setText("AI P stop");
                }else{
                    MySingleton.pack.runPersonTime = false;
                    PersonButton.setText("AI P start");
                }
            }
        });
        LawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(MySingleton.pack.runLawTime == false){
                    MySingleton.pack.runLawTime = true;
                    synchronized (MySingleton.pack.AILaw) {
                        MySingleton.pack.AILaw.notify();
                    }
                    LawButton.setText("AI L stop");
                }else{
                    MySingleton.pack.runLawTime = false;
                    LawButton.setText("AI L start");
                }
            }
        });
        combPerson.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(combPerson.getSelectedItem() == "Max"){
                    MySingleton.pack.AIPerson.setPriority(Thread.MAX_PRIORITY);
                }
                else if(combPerson.getSelectedItem() == "Med"){
                    MySingleton.pack.AIPerson.setPriority(Thread.NORM_PRIORITY);
                }else if(combPerson.getSelectedItem() == "Min"){
                    MySingleton.pack.AIPerson.setPriority(Thread.MIN_PRIORITY);
                }
            }
        });
        combLaw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(combLaw.getSelectedItem() == "Max"){
                    MySingleton.pack.AILaw.setPriority(Thread.MAX_PRIORITY);
                }
                else if(combLaw.getSelectedItem() == "Med"){
                    MySingleton.pack.AILaw.setPriority(Thread.NORM_PRIORITY);
                }else if(combLaw.getSelectedItem() == "Min"){
                    MySingleton.pack.AILaw.setPriority(Thread.MIN_PRIORITY);
                }
            }
        });

        Mvis.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(VisibleTime == false){
                    Visib.doClick();
                }
            }
        });
        MnotVis.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(VisibleTime == true){
                    notVisib.doClick();
                }
            }
        });
        MCheck.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e){
                if(Click == false){
                    Click = true;
                    Check.doClick();
                }else{
                    Click = false;
                }
            }
        });
        MStart.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                Start.doClick();
            }
        });
        MStop.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                Stop.doClick();
            }
        });
        console1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(console1.isSelected()){
                    ConsolWind.setVisible(true);
                }else{
                    ConsolWind.setVisible(false);
                    cons.setText(null);
                }
            }
        });
        cons.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER){

                    try {
                        int offs = cons.getLineStartOffset(cons.getLineCount()-1);
                        int len = cons.getLineEndOffset(cons.getLineCount()-1)-offs+1;
                        String comand =cons.getText(offs,len);
                        try{
                            PW.write(comand);
                        }catch(IOException exy){
                            exy.printStackTrace();
                        }
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                    char[] buffer = new char[100];
                    try{
                        PR.read(buffer);
                    }catch(IOException exy){
                        exy.printStackTrace();
                    }

                    String Ot = new StringBuilder().append(buffer).toString();

                    cons.setText(Ot);
                }
            }
        });
        Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save.Serialization();
            }
        });
        Load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save.Deserialization();
            }
        });


        frame.setVisible(true);
    }

    {
        width = 1000;
        height = 600;
        VisibleTime = false;
        VisibleStat = false;
        VisibleModalWindow = false;
        Click = false;
        sumTime = new AtomicLong(0);
        time = 0;
        TimeLabel = new JLabel("Time - " + sumTime + " sec");
        DP = new Drawing(width - 300, height - 70);
        frame = new JFrame();
        Scene = new JPanel();
        settingsScene = new JPanel();
        statistics = new JLabel();
        Window = new Habitat();
        Window.width = width;
        Window.height = height;
        PW=new PipedWriter();
        PR=new PipedReader();
        console = new Console(PR, PW);
    }

    Interface(){
        MySingleton.getInitialized();
        console.start();
        Frame();
    }
}