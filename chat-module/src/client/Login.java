package client;

import server.Server;
import util.ConnectionDB;
import util.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

public class Login extends JFrame  {
    private JButton btnLogin;
    private JButton btnCreateNewUser;
    private JLabel lbUser;
    private JLabel lbPort;
    private JLabel lbPassword;
    private JLabel lbTitle;
    private JLabel lbMessage;
    private JTextField textFieldUser;
    private JTextField textFieldPort;
    private JPasswordField passwordField;
    private String username;

    public Login() {
        super("Efetue Login para continuar...");
        initComponents();
        configComponents();
        insertComponents();
        insertActions();
        start();
    }

    public void initComponents() {
        btnLogin = new JButton("Entrar");
        btnCreateNewUser = new JButton("Criar novo usuário");
        lbUser = new JLabel("Usuário", SwingConstants.CENTER);
        lbPort = new JLabel("Porta", SwingConstants.CENTER);
        lbPassword = new JLabel("Senha", SwingConstants.CENTER);
        lbTitle = new JLabel("ECOCHAT", SwingConstants.CENTER);
        lbMessage = new JLabel("", SwingConstants.CENTER);
        textFieldUser = new JTextField();
        textFieldPort = new JTextField();
        passwordField = new JPasswordField();
    }

    public void configComponents() {
        this.setLayout(null);
        this.setMinimumSize(new Dimension(420, 450));
        this.setLocation(new Point(700, 320));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(new Color(54, 54, 54));

        lbTitle.setBounds(10, 10, 395, 100);
        lbTitle.setForeground(Color.WHITE);
        lbTitle.setFont(new Font("Roboto", Font.BOLD, 30));
        lbUser.setBounds(10, 120, 100, 40);
        lbUser.setForeground(Color.WHITE);

        lbPassword.setBounds(10, 170, 100, 40);
        lbPassword.setForeground(Color.WHITE);

        lbPort.setBounds(10, 220, 100, 40);
        lbPort.setForeground(Color.WHITE);

        textFieldUser.setBounds(120, 120, 285, 40);
        passwordField.setBounds(120, 170, 285, 40);
        textFieldPort.setBounds(120, 220, 285, 40);

        btnCreateNewUser.setBounds(10, 270, 220, 40);
        btnCreateNewUser.setBackground(new Color(54, 54, 54));
        btnCreateNewUser.setForeground(new Color(255, 255, 255));
        btnCreateNewUser.setBorderPainted(false);
        btnLogin.setBounds(310, 270, 90, 40);

        lbMessage.setBounds(10, 370, 400, 40);
        lbMessage.setForeground(Color.RED);
    }

    public void insertComponents() {
        this.add(btnLogin);
        this.add(btnCreateNewUser);
        this.add(lbUser);
        this.add(lbPort);
        this.add(lbPassword);
        this.add(lbTitle);
        this.add(textFieldUser);
        this.add(textFieldPort);
        this.add(passwordField);
        this.add(lbMessage);
    }

    public void insertActions() {
        btnLogin.addActionListener(event -> {
            try {
                String username = textFieldUser.getText().trim();
                textFieldUser.setText("");
                String pass = passwordField.getText().trim();
                passwordField.setText("");

                if (checkLogin(username, pass)) {

                    int port = Integer.parseInt(textFieldPort.getText());
                    textFieldPort.setText("");
                    Socket socket = new Socket(Server.HOST, Server.PORT);
                    String 	connectionInfo 	= 	(username 	+ 	":" 	+ socket.getLocalAddress().getHostAddress() + ":" + port);
                    Utils.sendMessage(socket, connectionInfo);

                    if (Utils.receiveMessage(socket).equals("SUCCESS")) {
                        new Home(socket, connectionInfo);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Algum usuario ja esta conectado neste host / porta, tente novamente!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario nao encontrado, tente novamente!");
                }
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao conectar! Verifique se o servidor esta em execucao!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Algum usuario ja esta conectado neste host / porta, tente novamente!");
            }
        });

        btnCreateNewUser.addActionListener(event -> {
            new Register();
            this.dispose();
        });
    }

    private boolean checkLogin(String username, String pass) throws SQLException {
        boolean success = false;
        Connection connection = ConnectionDB.getConnection();

        String query = "select * from public.user where username = '" + username + "';";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            success = resultSet.getString("username").equals(username) && resultSet.getString("password").equals(pass);
        }

        return success;
    }

    public void start() {
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new Login();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
