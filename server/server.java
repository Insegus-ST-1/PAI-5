import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.Signature;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Connection;
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


            InputStream input = clientSocket.getInputStream();

            byte[] customerBytes = input.read();
            String customer = customerBytes.toString();
            byte[] messageBytes = input.read();
            byte[] firma = input.read();

            Connection connection = DriverManager.getConnection("jdbc:sqlite:base.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // Wait only 30 seconds to connect

            String query = "SELECT * FROM certs WHERE name = ?";
            PreparedStatement p = connection.prepareStatement(query);
            p.setString(1,customer);
            ResultSet queryset = p.executeQuery();
            Signature sg = Signature.getInstance("SHA256withRSA");
            sg.initVerify(null);
            sg.update(messageBytes);
            sg.verify(firma);


            

            
        }

        

    }
}