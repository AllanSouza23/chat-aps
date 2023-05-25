package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(Secrets.url(), Secrets.user(), Secrets.pass());
            System.out.println("Conectado ao banco de dados!");
            return connection;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar com o banco!");
            throw new SQLException("Não foi possivel estabelecer uma conexao com o banco de dados!");
        }
    }

    public static void main(String[] args) throws SQLException {
        getConnection();
    }
}
