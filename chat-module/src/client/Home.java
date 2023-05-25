package client;

import util.Utils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home extends JFrame {

    private ArrayList<String> openedChats;
    private Map<String, ClientListener> connectedListeners;
    private ArrayList<String> connectedUsers;
    private String connectionInfo;
    private Socket socket;
    private ServerSocket serverSocket;
    private boolean running;

    private JLabel lbTitle;
    private JButton btnGetConnected;
    private JButton btnStartTalk;
    private JList jList;
    private JScrollPane scroll;

    public Home(Socket socket, String connectionInfo) {
        super("EcoChat - Home");
        this.connectionInfo = connectionInfo;
        this.socket = socket;
        initComponents();
        configComponents();
        insertComponents();
        insertActions();
        start();
    }

    private void initComponents() {
        running = false;
        serverSocket = null;
        openedChats = new ArrayList<>();
        connectedListeners = new HashMap<>();
        connectedUsers = new ArrayList<>();
        lbTitle = new JLabel("Usuario: " + connectionInfo.split(":")[0], SwingConstants.CENTER);
        btnGetConnected = new JButton("Atualizar Contatos");
        btnStartTalk = new JButton("Abrir Conversa");
        jList = new JList();
        scroll = new JScrollPane(jList);
    }

    private void configComponents() {
        this.setLayout(null);
        this.setMinimumSize(new Dimension(600, 480));
        this.setLocation(new Point(700, 320));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(new Color(54, 54, 54));

        lbTitle.setBounds(10, 10, 370, 40);
        lbTitle.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lbTitle.setForeground(Color.WHITE);

        btnGetConnected.setBounds(400, 10, 180, 40);
        btnGetConnected.setFocusable(false);

        btnStartTalk.setBounds(10, 400, 575, 40);
        btnStartTalk.setFocusable(false);
        jList.setBorder(BorderFactory.createTitledBorder("Usuarios Online"));
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scroll.setBounds(10, 60, 575, 335);

        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
    }

    private void insertComponents() {
        this.add(lbTitle);
        this.add(btnGetConnected);
        this.add(scroll);
        this.add(btnStartTalk);
    }

    private void insertActions() {
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                running = false;
                Utils.sendMessage(socket, "QUIT");
                System.out.println("Conexão encerrada.");
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });

        btnGetConnected.addActionListener(
                event -> getConnectedUsers());
        btnStartTalk.addActionListener(event -> openChat());
    }
    private void start() {
        this.pack();
        this.setVisible(true);
        startServer(this, Integer.parseInt(connectionInfo.split(":")[2]));
    }

    private void getConnectedUsers() {
        Utils.sendMessage(socket, "GET_CONNECTED_USERS");
        String response = Utils.receiveMessage(socket);

        jList.removeAll();
        connectedUsers.clear();
        for (String info : response.split(";")) {
            if(!info.equals(connectionInfo)) {
                connectedUsers.add(info);
            }
        }
        jList.setListData(connectedUsers.toArray());
    }

    public Map<String, ClientListener> getConnectedListeners() {
        return connectedListeners;
    }
    public ArrayList<String> getOpenedChats() {
        return openedChats;
    }

    public String getConnectionInfo() {
        return connectionInfo;
    }

    private void openChat() {
        int index = jList.getSelectedIndex();
        if (index != -1) {
            String connectionInfo = jList.getSelectedValue().toString();
            String[] splitted = connectionInfo.split(":");
            if (!openedChats.contains(connectionInfo)) {
                try {
                    Socket 	socket 	= 	new 	Socket(splitted[1], Integer.parseInt(splitted[2]));
                    Utils.sendMessage(socket, "OPEN_CHAT;" + this.connectionInfo);
                    ClientListener clientListener = new ClientListener(this, socket);
                    clientListener.setChat(new Chat(this, socket, 	connectionInfo, this.connectionInfo.split(":")[0]));
                    clientListener.setChatOpen(true);
                    connectedListeners.put(connectionInfo, clientListener);
                    openedChats.add(connectionInfo);
                    new Thread(clientListener).start();
                } catch (IOException e) {
                    System.err.println("[ERROR:Home.openChat] -> " + e.getMessage());
                }
            }
        }
    }

    private void startServer(Home home, int port) {
        new Thread() {
            @Override
            public void run() {
                running = true;
                try {
                    serverSocket = new ServerSocket(port);
                    System.out.println("Servidor cliente iniciado na porta: " + port +  "...");
                    while (running) {
                        Socket socket = serverSocket.accept();
                        ClientListener clientListener = 	new ClientListener(home, socket);
                        new Thread(clientListener).start();
                    }
                } catch (IOException e) {
                    System.err.println("[ERROR:Home.startServer] -> " + e.getMessage());
                }
            }
        }.start();
    }
}
