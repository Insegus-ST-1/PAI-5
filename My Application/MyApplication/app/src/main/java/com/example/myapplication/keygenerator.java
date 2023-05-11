package com.example.myapplication;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class keygenerator {

    public static void main(String[]args) throws NoSuchAlgorithmException {
        String c1 = "Customer 1";
        String c2 ="Customer 2";
        String c3 ="Customer 3";
        String c4 ="Customer 4";
        String c5 ="Customer 5";

        KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
        keygen.initialize(2048);
        KeyPair k1 = keygen.generateKeyPair();
        KeyPair k2 = keygen.generateKeyPair();
        KeyPair k3 = keygen.generateKeyPair();
        KeyPair k4 = keygen.generateKeyPair();
        KeyPair k5 = keygen.generateKeyPair();
        
        Map<String,KeyPair> values = new HashMap<>();
        values.put(c1, k1);
        values.put(c2, k2);
        values.put(c3, k3);
        values.put(c4, k4);
        values.put(c5, k5);

        Connection connection = null;
        try {
          // Create the connection to the database
          connection = DriverManager.getConnection("jdbc:sqlite:base.db");
          Statement statement = connection.createStatement();
          statement.setQueryTimeout(30); // Wait only 30 seconds to connect
    
          // Run commands for SQLite
          statement.executeUpdate("DROP TABLE IF EXISTS certs");
          statement.executeUpdate("CREATE TABLE certs (id INTEGER PRIMARY KEY, name STRING, pubkey BLOB, privkey BLOB)");
          for (String value : values.keySet()){
            String sql = "INSERT INTO certs(name, pubkey, privkey) VALUES (?,?,?)";
            PreparedStatement p = connection.prepareStatement(sql);
            p.setString(1, value);
            p.setBytes(2, values.get(value).getPublic().getEncoded());
            p.setBytes(3, values.get(value).getPrivate().getEncoded());
            p.executeUpdate();


          }
          
        } catch(SQLException e) {
          // If the error message is: "out of memory",
          // Probably error creating(permission) or database path
          System.err.println(e.getMessage());
        }
    
        finally {
          try {
            if(connection != null){
              connection.close();
            }
          } catch(SQLException e) {
            // Also failed to close the file
            System.err.println(e.getMessage());
          }
        }

    }


}
