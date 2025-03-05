import java.io.*;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

class Connection extends Thread implements Observer {
    BufferedReader in;
    BufferedWriter out;
    Socket clientSocket;
    TCPServer server;
    String clientName;

    public Connection(Socket aClientSocket, TCPServer server) {
        this.server = server;
        try {
            clientSocket = aClientSocket;
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            server.addObserver(this);
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void run() {
        try { // an echo server
            out.write("Input your username: ");
            clientName = in.readLine();
            while(true)
            {
                String message = in.readLine();
                this.server.addMessage(clientName, (String) message);
                update(this.server, message);
            }
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {/*close failed*/}
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        String message = (String) arg;
        o.notifyObservers(message);
    }
}
