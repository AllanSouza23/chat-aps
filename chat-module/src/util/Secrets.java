package util;

public class Secrets {
    private static final String user = "postgres";
    private static final String pass = "senhapostgres";
    private static final String url = "jdbc:postgresql://localhost:5432/chat";

    static String user() {
        return user;
    }

    static String pass() {
        return pass;
    }

    static String url() {
        return url;
    }
}
