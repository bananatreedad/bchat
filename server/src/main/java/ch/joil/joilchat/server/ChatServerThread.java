package ch.joil.joilchat.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by bananatreedad on 06/05/16.
 * <p>
 * This Class is responsible to handle the single chat session.
 */
public class ChatServerThread extends Thread {
    private ChatServer chatServer = null;
    private Socket socket = null;
    private int ID = -1;
    private Scanner scanner = null;

    private PrintWriter writer = null;

    private volatile Thread blinker = null;
    private String username = null;

    public ChatServerThread(ChatServer chatServer, Socket socket) {
        this.chatServer = chatServer;
        this.socket = socket;
        this.ID = socket.getPort();
    }

    public int getID() {
        return this.ID;
    }

    @Override
    public void run() {
        blinker = currentThread();
        System.out.println("Server Thread " + ID + "running.");

        while (blinker != null) {
            try {
                String s = scanner.nextLine();

                if (this.username == null) {
                    this.username = s;
                    System.out.println("Set username of ID " + ID + " to " + this.username);
//                    chatServer.handle(ID, username, s);
                } else {
                    chatServer.handle(ID, username, s);
                }

            } catch (NoSuchElementException ex) {
                System.out.println("Client with ID " + ID + " disconnected.");
                close();
                halt();
            }
        }
    }

    public void halt() {
        System.out.println("Halting ID " + ID);
        this.blinker = null;
    }

    public void send(String s) {
        writer.println(s);
    }

    public void close() {
        System.out.println("Closing connection to client with ID " + ID);
        try {
            if (socket != null) socket.close();
            if (scanner != null) scanner.close();
            if (writer != null) writer.close();
        } catch (IOException e) {
            System.out.println("Closing error...");
        }
    }

    public void open() {
        try {

            scanner = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            System.out.println("Error on opening Streams: " + e.getMessage());
        }
    }
}
