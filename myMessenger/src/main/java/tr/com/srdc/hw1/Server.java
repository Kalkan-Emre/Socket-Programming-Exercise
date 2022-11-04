package tr.com.srdc.hw1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static tr.com.srdc.hw1.DataBaseFunctions.checkTableIsEmpty;
import static tr.com.srdc.hw1.DataBaseFunctions.insertAdminToUsers;

public class Server {
    private static int active_client_number = 0;
    private static final int PORT = 8090;
    private static final int NUMBER_OF_CLIENTS = 10;
    private static ArrayList<ClientHandler> threads = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(NUMBER_OF_CLIENTS);

    public static void main(String[] args) throws IOException, SQLException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket client;
        boolean isFull=false;

        if(checkTableIsEmpty()) insertAdminToUsers();

        System.out.println("Waiting for client...");
        while(true) {
            if(!isFull){
                client = serverSocket.accept();
                active_client_number++;
                System.out.println("Connected to client "+active_client_number);
                ClientHandler thread = new ClientHandler(client);
                threads.add(thread);
                pool.execute(thread);
            }
            else{
                System.out.println("Server is full!!!");
            }
            if(active_client_number==NUMBER_OF_CLIENTS) isFull = true;
        }

    }
}


