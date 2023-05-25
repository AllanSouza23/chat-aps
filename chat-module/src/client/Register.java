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

public class Register extends JFrame{

    private JButton btnCreate;
    private JButton btnVoltar;
    private JLabel lbUser;
    private JLabel lbPassword;
    private JLabel lbTitle;
    private JLabel lbMessage;
    private JTextField textFieldUser;
    private JPasswordField passwordField;
    private String username;

    public Register() {
        super("Insira os dados abaixo para criar...");
        initComponents();
        configComponents();
        insertComponents();
        insertActions();
        start();
    }

    public void initComponents() {
        btnCreate = new JButton("Criar");
        btnVoltar = new JButton("Voltar");
        lbUser = new JLabel("Usuário", SwingConstants.CENTER);
        lbPassword = new JLabel("Senha", SwingConstants.CENTER);
        lbTitle = new JLabel("ECOCHAT", SwingConstants.CENTER);
        lbMessage = new JLabel("", SwingConstants.CENTER);
        textFieldUser = new JTextField();
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

        textFieldUser.setBounds(120, 120, 285, 40);
        passwordField.setBounds(120, 170, 285, 40);

        btnVoltar.setBounds(10, 270, 220, 40);
        btnVoltar.setBackground(new Color(54, 54, 54));
        btnVoltar.setForeground(new Color(255, 255, 255));
        btnVoltar.setBorderPainted(false);
        btnCreate.setBounds(310, 270, 90, 40);

        lbMessage.setBounds(10, 370, 400, 40);
        lbMessage.setForeground(Color.RED);
    }

    public void insertComponents() {
        this.add(btnCreate);
        this.add(btnVoltar);
        this.add(lbUser);
        this.add(lbPassword);
        this.add(lbTitle);
        this.add(textFieldUser);
        this.add(passwordField);
        this.add(lbMessage);
    }

    public void insertActions() {
        btnCreate.addActionListener(event -> {
            try {
                String username = textFieldUser.getText().trim();
                textFieldUser.setText("");
                String pass = passwordField.getText().trim();
                passwordField.setText("");

                if (!checkLogin(username, pass)) {
                        insertUser(username, pass);
                        new Login();
                        this.dispose();
                } else {
                        JOptionPane.showMessageDialog(null, "Usuario já existe, tente novamente!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao conectar! Verifique se o servidor esta em execucao!");
            }
        });

        btnVoltar.addActionListener(event -> {
            new Login();
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

    void insertUser(String username, String pass) throws SQLException {
        Connection connection = ConnectionDB.getConnection();

        String query = "insert into public.users values (" + username + ", " + pass + ");";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeQuery();
    }

    public void start() {
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new Register();
    }
}
