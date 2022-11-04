package tr.com.srdc.hw1;

import tr.com.srdc.hw1.entity.Message;
import tr.com.srdc.hw1.entity.User;

import java.sql.*;
import java.util.ArrayList;


public class DataBaseFunctions {

    static Db objConnectDb = new Db();
    static Connection connection = objConnectDb.getConnection();

    public static void insertAdminToUsers() {
        Statement statement;
        try{
            String query = "insert into users(username,password,name,surname,birth_date,gender,is_active,user_type,email,address) " +
                                        "values('emre1','12345','emre','kalkan','2001-10-11','male',false,'admin','kalkan_emre@outlook.com','Ankara')";
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveMessageToDB(Message message) {
        PreparedStatement ps;
        try{
            String query = "insert into message(send_to,sent_from,message) values(?,?,?)";
            ps = connection.prepareStatement(query);
            ps.setString(1, message.getTo());
            ps.setString(2, message.getFrom());
            ps.setString(3, message.getMessage());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<Message> getInbox(String username) throws SQLException {
        ArrayList<Message> inbox = new ArrayList<>();
        PreparedStatement ps;
        ResultSet rs = null;
        try{
            String query = "select *  from message where send_to = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1,username);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (rs.next()) {
            Message msg = new Message(rs.getString(1),rs.getString(2),rs.getString(3));
            inbox.add(msg);
        }
        return inbox;
    }

    public static ArrayList<Message> getOutbox(String username) throws SQLException {
        ArrayList<Message> outbox = new ArrayList<>();
        PreparedStatement ps;
        ResultSet rs = null;
        try{
            String query = "select *  from message where sent_from = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1,username);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (rs.next()) {
            Message msg = new Message(rs.getString(1),rs.getString(2),rs.getString(3));
            outbox.add(msg);
        }
        return outbox;
    }

    public static boolean findUserByUsernameAndPassword(String username, String password) throws SQLException {
        PreparedStatement ps;
        ResultSet rs = null;
        try{
            String query = "select *  from users where username = ? and password = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1,username);
            ps.setString(2,password);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs.next();
    }

    public static boolean findUserByUsername(String username) throws SQLException {
        PreparedStatement ps;
        ResultSet rs = null;
        try{
            String query = "select *  from users where username = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1,username);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert rs != null;
        return rs.next();
    }

    public static boolean isActive(String username) throws SQLException {
        PreparedStatement ps;
        ResultSet rs = null;
        try{
            String query = "select *  from users where username = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1,username);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert rs != null;
        if(rs.next())return rs.getBoolean(8);
        else return false;
    }

    public static boolean isAdmin(String username) throws SQLException {
        PreparedStatement ps;
        ResultSet rs = null;
        try{
            String query = "select *  from users where username = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1,username);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assert rs != null;
        if(rs.next())return rs.getString(9).equals("admin");
        else return false;
    }


    public static ArrayList<User> listUsersDB() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement ps;
        ResultSet rs = null;
        try{
            String query = "select *  from users";
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (rs.next()) {
            User newUser = new User(rs.getString(2),rs.getString(3),rs.getString(4),
                    rs.getString(5),rs.getDate(6), rs.getString(7),rs.getString(10),
                    rs.getString(11),rs.getString(9),rs.getBoolean(8));
            users.add(newUser);
        }
        return users;
    }

    public static boolean checkTableIsEmpty() throws SQLException {
        PreparedStatement ps;
        ResultSet rs=null;
        try{
            String query = "select count(*) from users";
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        rs.next();
        return rs.getInt(1)==0;
    }

    public static void insertUser(User user) {
        PreparedStatement ps;
        try{
            String query = "insert into users(username,password,name,surname,birth_date,gender,is_active,user_type,email,address) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            ps = connection.prepareStatement(query);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.setString(3,user.getName());
            ps.setString(4,user.getSurname());
            ps.setDate(5,user.getBirthDate());
            ps.setString(6,user.getGender());
            ps.setBoolean(7,false);
            ps.setString(8,user.getUserType());
            ps.setString(9,user.getEmail());
            ps.setString(10,user.getAddress());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void removeUserDB(String username){
        PreparedStatement ps;
        try{
            String query = "delete from users where username = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1,username);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePasswordDB(String username,String newPassword){
        PreparedStatement ps;
        try{
            String query = "update users set password = ? where username = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1,newPassword);
            ps.setString(2,username);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void logIn(String username){
        PreparedStatement ps;
        try{
            String query = "update users set is_active = true where username = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1,username);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void logOut(String username){
        PreparedStatement ps;
        try{
            String query = "update users set is_active = false where username = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1,username);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateUsernameDB(String username,String newUsername){
        PreparedStatement ps;
        try{
            String query = "update users set username = ? where username = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1,newUsername);
            ps.setString(2,username);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void updateAddressDB(String username,String newAddress){
        PreparedStatement ps;
        try{
            String query = "update users set address = ? where username = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1,newAddress);
            ps.setString(2,username);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
