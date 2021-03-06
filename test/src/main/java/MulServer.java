import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MulServer {
    private static void handleConnection(Socket client) throws IOException {
        Scanner in = new Scanner(client.getInputStream());
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);

        while (true) {
            if (in.hasNextLine()) {
                String string = in.nextLine();
                System.out.println("User: " + string);
                out.println("User: " + string);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(3141);

        while (true) {
            Socket client = null;

            try {
                client = server.accept();
                handleConnection(client);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (client != null)
                    try {
                        client.close();
                    } catch (IOException e) {
                    }
            }
        }
    }
}
