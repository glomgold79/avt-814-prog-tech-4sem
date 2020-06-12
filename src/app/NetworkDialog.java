package app;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class NetworkDialog extends JDialog {

    private JLabel statusLabel;
    private JList<String> clients;
    private Client client;
    private String[] items;
    private List<Integer> clientList;

    NetworkDialog(GUI owner) {
        super(owner, "Текущие объекты", false);
        setLayout(new BorderLayout());
        setBounds(700, 380, 400, 250);

        JPanel clientsPanel = new JPanel(new BorderLayout());
        clientsPanel.setBorder(new EmptyBorder(10, 15, 10, 10));
        add(clientsPanel, BorderLayout.CENTER);

        clients = new JList<>();
        clientsPanel.add(clients);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setPreferredSize(new Dimension(150, 170));
        buttons.setBorder(new EmptyBorder(10, 10, 10, 15));
        buttons.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(buttons, BorderLayout.WEST);

        JButton connectButton = new JButton("Подключится");
        connectButton.addActionListener(e -> connect());
        connectButton.setPreferredSize(new Dimension(125, 25));
        connectButton.setFocusable(false);
        buttons.add(connectButton);

        JButton disconnectButton = new JButton("Отключится");
        disconnectButton.addActionListener(e -> disconnect());
        disconnectButton.setFocusable(false);
        disconnectButton.setPreferredSize(connectButton.getPreferredSize());
        buttons.add(disconnectButton);

        JButton getButton = new JButton("Передать");
        getButton.addActionListener(e -> get());
        getButton.setPreferredSize(connectButton.getPreferredSize());
        getButton.setFocusable(false);
        buttons.add(getButton);



        statusLabel = new JLabel("Соединения нет");
        buttons.add(statusLabel);

        setVisible(false);
    }

    private void get() {
        if (clientList == null || clientList.isEmpty()) return;
        if (clients.getSelectedIndex() == -1) return;

        client.swapWithClient(clientList.get(clients.getSelectedIndex()));
    }

    public void showText(String text) {
        statusLabel.setText(text);
    }

    private void connect() {
        client = new Client(this);
        client.start();
    }

    private void disconnect() {
        client.disconnect();
        clients.setListData(new String[0]);

    }

    public void showList(List<Integer> ids,int id) {
        clientList = ids;
        items = null;
        items = new String[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i) == id){
                items[i] = "Клиент №" + ids.get(i) + " <- Это вы";
            }else{
                items[i] = "Клиент №" + ids.get(i);
            }

            System.out.println(items[i]);
        }
        clients.setListData(items);
    }
}
