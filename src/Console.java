import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Console extends JDialog implements Runnable {
    private PipedOutputStream outputFromField;
    private final PipedInputStream inputFromField;
    private Scanner fieldInput;
    private PrintStream fieldOutput;
    private JTextField inputField;
    private JTextArea outputArea;


    Console(GUI owner){
        super(owner, "Консоль", false);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setLocation(650, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 250);
        setVisible(true);
        requestFocus();

        JLabel commandHint = new JLabel("<html>Доступные команды: <br>'print drone' - количество трутней " +
                "<br>'print worker'- количество пчёл рабочих");
        add(commandHint);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setPreferredSize(new Dimension(getWidth() - 25, 100));
        outputArea.setBackground(Color.BLACK);
        outputArea.setForeground(Color.WHITE);
        add(outputArea);

        inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(getWidth() - 25, 25));
        add(inputField);

        outputFromField = new PipedOutputStream();
        inputFromField = new PipedInputStream();
        try {
            outputFromField.connect(inputFromField);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        fieldInput = new Scanner(inputFromField);
        fieldOutput = new PrintStream(outputFromField);


        inputField.addActionListener(e -> {
            String text = inputField.getText();
            fieldOutput.println(text);
            inputField.setText("");
            synchronized (inputFromField) {
                inputFromField.notify();
            }
        });
        Thread mainProgram = new Thread(this);
        mainProgram.start();
    }
    public void run() {
        while (fieldInput.hasNextLine()) {
            String line = fieldInput.nextLine();
            outputArea.append(line + "\n");
            System.out.println("Program recieved input: " + line);

            try {
                if (line.contains("print drone")) {

                    outputArea.append(Habitat.numberOfDrones+" трутней" + "\n");
                } else if (line.contains("print worker")) {
                    outputArea.append(Habitat.numberOfWorkers+" рабочих" + "\n");
                }
                else {
                    throw new Exception();
                }
            } catch (Exception e2) {
                JOptionPane.showMessageDialog(null,
                        "Введена неверная команда.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
