import javax.swing.*;
import java.awt.*;

class VPanel extends JPanel {
    VPanel(int x, int y){
        setPreferredSize(new Dimension(x, y));
        setLayout(new FlowLayout(FlowLayout.CENTER));
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        for (int i = 0; i < Habitat.array.size(); i++) {
            g.drawImage(Habitat.array.get(i).img.getImage(), Habitat.array.get(i).getX(),
                    Habitat.array.get(i).getY(), null);
        }
    }
}