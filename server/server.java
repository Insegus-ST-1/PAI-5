import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.net.ssl.*;

public class server{

    public static void main(String[] args) throws IOException, InterruptedException{
        SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(7070);
        serverSocket.setReuseAddress(true);

        System.out.println("Waiting for connection...");

        while (true) {
            // Accept connection
            SSLSocket clientSocket = (SSLSocket) serverSocket.accept();

            // Open BufferedReader for reading data from client
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Open PrintWriter for writing data to client
		    PrintWriter output = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            String hello = input.readLine();
            System.out.print(hello);
            output.print(" hello client");
        }

        

    }
}