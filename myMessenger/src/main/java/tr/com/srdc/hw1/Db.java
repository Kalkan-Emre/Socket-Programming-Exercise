package tr.com.srdc.hw1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Db {
    public Connection getConnection() {
        String url = "jdbc:postgresql://localhost:5432/user";
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url,"postgres","miradavatos");
            if(connection!=null) System.out.println("Connected");
            else System.out.println("Connection failed");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
