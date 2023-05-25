package client;

import util.Utils;
import java.io.IOException;
import java.net.Socket;

public class ClientListener implements Runnable {

    private boolean running;
    private boolean chatOpen;
    private Socket socket;
    private Home home;
    private String connectionInfo;
    private Chat chat;

    public ClientListener(Home home, Socket socket) {
        this.chatOpen = false;
        this.running = false;
        this.home = home;
        this.socket = socket;
        this.connectionInfo = null;
        this.chat = null;
    }

    public boolean isChatOpen() {
        return chatOpen;
    }

    public void setChatOpen(boolean chatOpen) {
        this.chatOpen = chatOpen;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    @Override
    public void run() {
        running = true;
        String message;

        while (running) {
            message = Utils.receiveMessage(socket);
            if (message == null || message.equals("CHAT_CLOSE")) {
                if (chatOpen) {
                    home.getOpenedChats().remove(connectionInfo);
                    home.getConnectedListeners().remove(connectionInfo);
                    chatOpen = false;
                    try {
                        socket.close();
                    } catch (IOException e) {
                        System.err.println("[ERROR:ClientListener.run] -> " + e.getMessage());
                    }
                    chat.dispose();
                }
                running = false;
            } else {
                String[] fields = message.split(";");
                if (fields.length > 1) {
                    if (fields[0].equals("OPEN_CHAT")) {
                        connectionInfo = fields[1];
                        if(!chatOpen) {
                            home.getOpenedChats().add(connectionInfo);
                            home.getConnectedListeners().put(connectionInfo, this);
                            chatOpen = true;
                            chat = new Chat(home, socket, connectionInfo, home.getConnectionInfo().split(":")[0]);
                        }
                    }
                    else if (fields[0].equals("MESSAGE")) {
                        String msg = "";
                        for (int i = 1; i < fields.length; i++) {
                            msg += fields[i];
                            if (i > 1) {
                                msg += ";";
                            }
                        }
                        chat.appendMessage(msg);
                    }
                }
            }
            System.out.println("Message: " + message);
        }
    }
}
