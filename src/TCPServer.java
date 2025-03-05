import java.net.*;
import java.io.*;
import java.util.Observable;

public class TCPServer extends Observable {
    public void startServer(int port) {
        try{
            int serverPort = port;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while(true) {
                Socket clientSocket = listenSocket.accept();
                new Thread(new Connection(clientSocket, this)).start();
                Connection c = new Connection(clientSocket, this);
            }
        } catch(IOException e) {System.out.println("Listen :"+e.getMessage());}
    }

    public void addMessage(String nickname, String message)
    {
        String fullMessage = nickname + ": " + message;
        setChanged();
        notifyObservers(fullMessage);
    }
}