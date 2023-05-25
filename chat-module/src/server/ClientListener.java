package server;

import util.Utils;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class ClientListener implements Runnable {

    private String connectionInfo;
    private Socket socket;
    private Server server;
    private boolean running;
    public ClientListener(String connectionInfo, Socket socket, Server server) {
        this.connectionInfo = connectionInfo;
        this.socket = socket;
        this.server = server;
        this.running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        running = true;
        String message;

        while (running) {
            message = Utils.receiveMessage(socket);
            if (message.equals("QUIT")) {
                server.getClients().remove(connectionInfo);
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("[ERROR:ClientListener.run] -> 	" 	+ e.getMessage());
                }
                running = false;
            } else if (message.equals("GET_CONNECTED_USERS")) {
                System.out.println("Solicitação para atualizar lista de contatos...");
                String response = "";
                for 	(Map.Entry<String, 	ClientListener> 	pair 	: server.getClients().entrySet()) {
                    response += (pair.getKey() + ";");
                }
                Utils.sendMessage(socket, response);
            } else {
                System.out.println("Recebido: " + message);
            }
        }
    }
}

