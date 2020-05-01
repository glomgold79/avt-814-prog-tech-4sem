import java.io.*;

public class Saver {
/* Порядок файла:
* P (индекс выбранного элемента)
* K (индекс выбранного элемента)
* N обыкновенных (текстовое поле)
* N альбиносов (текстовое поле)
* Время жизни обычных
* Время жизни альбиносов
* Приоритет обычных
* Приоритет альбиносов
*/
    private GUI gui;

    public Saver(GUI gui) {
        this.gui = gui;
    }

    public void SaveConfigurations() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                super.run();
                File file = new File("Configuration.txt");
                try {
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(gui.jComboBox.getSelectedIndex() + " ");
                    fileWriter.write(gui.jList.getSelectedIndex() + " ");
                    fileWriter.write(gui.jTextFieldNOrdinary.getText() + " ");
                    fileWriter.write(gui.jTextFieldNAlbino.getText() + " ");
                    fileWriter.write(gui.jTextFieldLiveTimeOrdinary.getText() + " ");
                    fileWriter.write(gui.jTextFieldLiveTimeAlbino.getText() + " ");
                    fileWriter.write(gui.jComboBoxOrdinaryPriority.getSelectedIndex() + " ");
                    fileWriter.write(Integer.toString(gui.jComboBoxAlbinoPriority.getSelectedIndex()));
                    fileWriter.flush();
                    fileWriter.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
