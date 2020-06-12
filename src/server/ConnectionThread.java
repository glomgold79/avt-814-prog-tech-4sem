package server;
import app.Bee;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class ConnectionThread extends Thread {
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;
    private Socket socket;
    private int id;
    private boolean connected = true;

    ConnectionThread(Socket socket, int id) {
        this.socket = socket;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            outStream = new ObjectOutputStream(socket.getOutputStream());
            inStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Запущен поток для клиента " + id);

            outStream.writeObject(Server.ids);
            outStream.writeInt(id);
            outStream.flush();

            while (connected) {
                System.out.println("read");
                String command = inStream.readUTF();
                System.out.println(command);
                switch (command) {
                    case Server.SWAP_WITH_CLIENT:
                        int targetID = inStream.readInt();
                        List<Bee> Drones = (ArrayList<Bee>) inStream.readObject();
                        Server.clients.get(targetID).outStream.writeUTF("getBees");
                        Server.clients.get(targetID).outStream.writeObject(Drones);

                        Server.clients.get(targetID).outStream.flush();
                        break;

                    case Server.DISCONNECT:

                        connected = false;
                        disconnect();
                        break;
                    default:
                        System.err.println("Unknown command");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
            Server.updateAll();
        }
    }

    private void disconnect() {
        try {
            System.out.println("disconnect");
            if (inStream != null) inStream.close();
            if (outStream != null) outStream.close();
            socket.close();
            connected = false;
            Server.clients.remove(id);
            for (int i = 0; i < Server.ids.size(); i++)
                if (Server.ids.get(i) == id)
                    Server.ids.remove(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateClients() {
        try {
            outStream.writeUTF("updateClients");
            List<Integer> isds = new ArrayList<>();
            for (int i = 0; i < Server.ids.size(); i++)
                isds.add(Server.ids.get(i));
            System.out.println(isds);
            outStream.writeObject(isds);
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

