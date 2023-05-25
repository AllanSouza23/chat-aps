package client;

import util.Utils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Chat extends JFrame {
    private JLabel lbTitle;
    private JEditorPane messages;
    private JTextField textFieldMessage;
    private JButton btnMessage;
    private JPanel panel;
    private JScrollPane scroll;
    private Home home;
    private Socket socket;
    private String connectionInfo;
    private String title;
    private ArrayList<String> messageList;
    public Chat(Home home, Socket socket, String connectionInfo, String title) {
        super("Chat de " + title);
        this.connectionInfo = connectionInfo;
        this.title = title;
        this.home = home;
        this.socket = socket;
        initComponents();
        configComponents();
        insertComponents();
        insertActions();
        start();
    }

    private void initComponents() {
        messageList = new ArrayList<>();
        lbTitle = new JLabel(connectionInfo.split(":")[0], SwingConstants.CENTER);
        messages = new JEditorPane();
        scroll = new JScrollPane(messages); textFieldMessage = new JTextField();
        btnMessage = new JButton("Enviar");
        panel = new JPanel(new BorderLayout());
    }

    private void configComponents() {
        this.setMinimumSize(new Dimension(480, 720));         this.setLayout(new BorderLayout());         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        messages.setContentType("text/html");
        messages.setEditable(false);

        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        btnMessage.setSize(100, 40);
    }

    private void insertComponents() {
        this.add(lbTitle, BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);
        this.add(panel, BorderLayout.SOUTH);
        panel.add(textFieldMessage, BorderLayout.CENTER);
        panel.add(btnMessage, BorderLayout.EAST);
    }

    private void insertActions() {
        btnMessage.addActionListener(event -> send());
        textFieldMessage.addKeyListener(new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                send();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    });

        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                Utils.sendMessage(socket, "CHAT_CLOSE");
                home.getOpenedChats().remove(connectionInfo);
                home.getConnectedListeners().get(connectionInfo).setChatOpen(false);
                home.getConnectedListeners().get(connectionInfo).setRunning(false);
                home.getConnectedListeners().remove(connectionInfo);
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
    }
    public void appendMessage(String received) {
        messageList.add(received);
        String message = "";
        for(String str : messageList) {
            message += str;
        }
        messages.setText(message);
    }

    public void send() {
        if (textFieldMessage.getText().length() > 0) { DateFormat df = new SimpleDateFormat("hh:mm:ss");
            String messageToOther = "<b>[" + df.format(new Date()) + "] " + title + "</b>: " + textFieldMessage.getText() + "<br>";
            String message = "<b>[" + df.format(new Date()) + "] Você</b>: " + textFieldMessage.getText() + "<br>";
            Utils.sendMessage(socket, "MESSAGE;" + messageToOther);
            appendMessage(message);
            textFieldMessage.setText("");
        }
    }
    private void start() {
        this.pack();
        this.setVisible(true);
    }
}
