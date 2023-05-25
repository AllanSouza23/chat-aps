package util;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Utils {

    public static boolean sendMessage(Socket socket, String message) {
        try {
            ObjectOutputStream 	outputStream 	= 	new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            outputStream.writeObject(message);
            return true;
        } catch (IOException e) {
            System.err.println("[ERROR:sendMessage] -> " + e.getMessage());
        }
        return false;
    }

    public static String receiveMessage(Socket socket) {
        String response = null;

        try {
            ObjectInputStream 	inputStream = new ObjectInputStream(socket.getInputStream());
            response = (String) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[ERROR:receiveMessage] -> " + e.getMessage());
        }

        return response;
    }
}

