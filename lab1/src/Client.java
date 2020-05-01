import java.io.*;
import java.util.Scanner;
import java.util.Vector;

public class Client implements TCPConnectionListener {

    private static final  String IP = "localhost";
    private static final int port = 3333;
    private static TCPConnection tcpConnection = null;
    private static Vector<String> UsersOnline = new Vector<>();

    public Client() {
        System.out.println("Client is running...");
        try {
            tcpConnection = new TCPConnection(this, IP, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void RequestConfigs(String SocketPort) {
        tcpConnection.sendString("Request " + SocketPort);
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        System.out.println("Connection ready");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        System.out.println(value);
        if (value.substring(0,7).equals("Config ")) {
            File file = new File("Configuration.txt");
            try (Scanner scanner = new Scanner(file)) {
                String string = scanner.nextLine();
                tcpConnection.sendString("Command " + value.substring(7) + " " + string);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (value.substring(0,5).equals("File ")) {
            File file = new File("Configuration.txt");
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(value.substring(6));
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (value.substring(0,5).equals("User ")) {
            if (!(UsersOnline.indexOf(value.substring(5)) >= 0) && Integer.parseInt(value.substring(5)) != tcpConnection.getSocketPort())
                UsersOnline.add(value.substring(5));
        }
    }

    @Override
    public void onDisconnection(TCPConnection tcpConnection) {
        System.out.println("Disconnected");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("Connection exception: " + e);
    }

    public Vector<String> getUsersOnline() {
        return  UsersOnline;
    }

}
