import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

public class Server implements TCPConnectionListener{

    public  static  void  main(String[] args) {
        new Server();

    }

    private final HashMap<Integer,TCPConnection> connections = new HashMap<>();

    private Server() {
        System.out.println("Server is running...");
        try (ServerSocket serverSocket = new ServerSocket(3333)) {
            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.put(tcpConnection.getServerSocketPort(),tcpConnection);
        sendToAllConnections();
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        if (value.substring(0, 8).equals("Request ")) {
            int key = Integer.parseInt(value.substring(8));
            if (connections.get(key) != null) {
                connections.get(key).sendString("Config " + tcpConnection.getServerSocketPort());
            }
        } else if (value.substring(0, 8).equals("Command ")) {
            int key = Integer.parseInt(value.substring(8,value.indexOf(' ',8)));
            if (connections.get(key) != null) {
                connections.get(key).sendString("File " + value.substring(value.indexOf(' ',9)));
            }
        }
    }

    @Override
    public synchronized void onDisconnection(TCPConnection tcpConnection) {
        connections.remove(tcpConnection.getServerSocketPort());
        sendToAllConnections();
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void sendToAllConnections() {
        connections.forEach((key,tcpConnection) ->
            connections.forEach((key1,tcpConnection1) ->
                tcpConnection.sendString("User " + tcpConnection1.toString())));
    }
}
