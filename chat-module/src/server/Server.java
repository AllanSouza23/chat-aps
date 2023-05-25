package server;

import util.Utils;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 1234;

    private ServerSocket serverSocket;
    private Map<String, ClientListener> clients;

    public Server() {
        try {
            String connectionInfo;
            clients = new HashMap<String, ClientListener>();
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado no host: " + HOST + " e na porta: " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                connectionInfo = Utils.receiveMessage(socket);

                if (checkLogin(connectionInfo)) {
                    ClientListener	clientListener = new ClientListener(connectionInfo, socket, this);
                    clients.put(connectionInfo, clientListener);
                    Utils.sendMessage(socket, "SUCCESS");
                    new Thread(clientListener).start();
                } else {
                    Utils.sendMessage(socket, "ERROR");
                }
            }
        } catch (IOException e) {
            System.err.println("[ERROR:Server] -> " + e.getMessage());
        }
    }

    public Map<String, ClientListener> getClients() {
        return clients;
    }

    private boolean checkLogin(String connectionInfo) {
        String[] splitted = connectionInfo.split(":");
        for (Map.Entry<String, ClientListener> pair : clients.entrySet()) {
            String[] parts = pair.getKey().split(":");
            if (parts[0].toLowerCase().equals(splitted[0].toLowerCase())) {
                return false;
            } else if ((parts[1] + parts[2]).equals((splitted[1] + splitted[2]))) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        new Server();
    }
}

