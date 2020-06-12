package app;

import server.Server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client extends Thread {

    private Socket socket;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;
    private boolean connected = true;
    private String command;

    private NetworkDialog owner;
    private int targetId;
    private int id;

    Client(NetworkDialog owner) {
        this.owner = owner;
    }

    @Override
    public void run() {
        try {
            String host = "localhost";
            int port = 1337;
            socket = new Socket(host, port);
            connected = true;
            System.out.println("Соединение установлено");

            outStream = new ObjectOutputStream(socket.getOutputStream());
            inStream = new ObjectInputStream(socket.getInputStream());
            owner.showText("Соединение установлено");

            List<Integer> ids = (List<Integer>) inStream.readObject();
            id = inStream.readInt();
            owner.showList(ids, id);

            while (connected) {
                if (inStream.available() == 0) {
                    if (command != null) {
                        switch (command) {
                            case "get":
                                outStream.writeUTF(Server.SWAP_WITH_CLIENT);
                                outStream.writeInt(targetId);
                                outStream.writeObject(Habitat.getBee());
                                outStream.flush();

                                command = null;
                                break;
                            case "disconnect":
                                System.out.println("disc");
                                outStream.writeUTF(Server.DISCONNECT);
                                outStream.flush();
                                command = null;
                                break;
                            default:
                                command = null;
                                break;
                        }
                    }
                } else {
                    String serverCommand = inStream.readUTF();
                    System.out.println(serverCommand);

                    switch (serverCommand) {
                        case "getBees":

                            ArrayList<Bee> bees = (ArrayList<Bee>) inStream.readObject();

                            for (int i = 0; i < bees.size(); i++) {
                                Habitat.array.add(bees.get(i));

                            }
                            break;

                        case "updateClients":
                            ids = (List<Integer>) inStream.readObject();
                            System.out.println("ids");
                            System.out.println(ids);
                            owner.showList(ids, id);

                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    public void swapWithClient(int id) {
        command = "get";
        targetId = id;
    }

    public void disconnect() {
        command = "disconnect";
        owner.showText("Соединения нет");
    }

    private void closeAll() {
        System.out.println("closeAll");
        connected = false;
        try {
            inStream.close();
            outStream.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
