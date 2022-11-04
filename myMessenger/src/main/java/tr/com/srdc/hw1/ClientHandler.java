package tr.com.srdc.hw1;

import tr.com.srdc.hw1.entity.Message;
import tr.com.srdc.hw1.entity.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static tr.com.srdc.hw1.DataBaseFunctions.*;


public class ClientHandler implements Runnable{

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private String username = null;
    private String[] protocolContents;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.socket = clientSocket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            while(true) {
                boolean authenticated = signIn();
                while (authenticated) {
                    logIn(username);
                    System.out.println("[SERVER] Waiting for command...");
                    String incomingProtocol = in.readLine();
                    protocolContents = incomingProtocol.split("-");
                    String command = protocolContents[0];
                    if (command.equals("quit")) break;
                    switch (command) {
                        case "help":
                            help();
                            break;
                        case "send message":
                            sendMessage();
                            break;
                        case "inbox":
                            inbox();
                            break;
                        case "outbox":
                            outbox();
                            break;
                        case "list users":
                            listUsers();
                            break;
                        case "remove user":
                            removeUser();
                            break;
                        case "update password":
                            updatePassword();
                            break;
                        case "add user":
                            addUser();
                            break;
                        case "log out":
                            out.println("log out");
                            logOut(username);
                            authenticated = false;
                            break;
                        case "update username":
                            updateUsername();
                            break;
                        case "update address":
                            updateAddress();
                            break;
                        default:
                            out.println("Wrong command");
                    }
                }
            }
        }   catch (IOException | SQLException e){
            System.err.println("IOException in ClientHandler");
        }
        finally {
            out.close();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage() throws IOException, SQLException {
        String sendTo;
        String msgToSend;
        String responseToClient = "Send your message using the format: username of receiver-your message";
        Message msg;

        out.println(responseToClient);
        System.out.println("[SERVER] Waiting for message...");

        sendTo = protocolContents[1];
        msgToSend = protocolContents[2];

        if(findUserByUsername(sendTo)){
            out.println("User found");
            msg = new Message(sendTo, username,msgToSend);
            saveMessageToDB(msg);
        }else{
            out.println("User cannot found!");
        }
    }

    private void listUsers() throws SQLException {
        if(isAdmin(username)){
            String responseToClient = "list users";
            out.println(responseToClient);
            ArrayList<User> users = listUsersDB();

            out.println(users.size());
            for (User user : users) {
                out.println("Username: "+user.getUserName()+"   User Type: "+user.getUserType()+
                        "  Email: "+user.getEmail()+"  Address: "+user.getAddress());
            }
        }
        else{
            out.println("You are not an admin");
        }
    }

    private void removeUser() throws SQLException, IOException {
        if(isAdmin(username)){
            String responseToClient = "remove user";
            out.println(responseToClient);
            String inputUsername = protocolContents[1];
            if(findUserByUsername(inputUsername)) {
                removeUserDB(inputUsername);
                out.println("User removed");
            }
            else{
                out.println("User cannot found");
            }
        }
        else{
            out.println("You are not an admin");
        }
    }

    private void updatePassword() throws SQLException, IOException {
        String responseToClient = "update password";
        out.println(responseToClient);
        String newPassword = protocolContents[1];
        updatePasswordDB(username,newPassword);
    }

    private void updateUsername() throws SQLException, IOException {
        if(isAdmin(username)){
            String responseToClient = "update username";
            out.println(responseToClient);
            String usernameInput = protocolContents[1];
            String newUsernameInput = protocolContents[2];
            if(findUserByUsername(usernameInput)) {
                updateUsernameDB(usernameInput,newUsernameInput);
                out.println("Username updated");
            }
            else{
                out.println("User cannot found");
            }
        }
        else{
            out.println("You are not an admin");
        }
    }

    private void updateAddress() throws SQLException, IOException {
        if(isAdmin(username)){
            String responseToClient = "update address";
            out.println(responseToClient);
            String usernameInput = protocolContents[1];
            String newAddressInput = protocolContents[2];
            if(findUserByUsername(usernameInput)) {
                updateAddressDB(usernameInput,newAddressInput);
                out.println("Address updated");
            }
            else{
                out.println("User cannot found");
            }
        }
        else{
            out.println("You are not an admin");
        }
    }

    private void addUser() throws SQLException, IOException {
        if(isAdmin(username)){
            String responseToClient = "add user";
            out.println(responseToClient);
            System.out.println(Arrays.toString(protocolContents));
            String userName = protocolContents[1];
            String password = protocolContents[2];
            String name= protocolContents[3];
            String surname= protocolContents[4];
            Date birthDate= Date.valueOf(protocolContents[5]+"-"+protocolContents[6]+"-"+protocolContents[7]);
            String gender= protocolContents[8];
            String email= protocolContents[9];
            String address= protocolContents[10];
            String userType = protocolContents[11];
            boolean isActive = false;
            User newUser = new User(userName,password,name,surname,birthDate,gender,email,address,userType,isActive);
            insertUser(newUser);
            out.println("New user has been added");
        }
        else{
            out.println("You are not an admin");
        }
    }

    private void inbox() throws SQLException {
        String responseToClient = "message box";
        ArrayList<Message> messageArraysList = getInbox(username);
        out.println(responseToClient);
        out.println(messageArraysList.size());
        for (Message message : messageArraysList) {
            out.println("From: " + message.getFrom() + " Message: " + message.getMessage());
        }
    }

    private void outbox() throws SQLException {
        String responseToClient = "message box";
        ArrayList<Message> messageArraysList = getOutbox(username);
        out.println(responseToClient);
        out.println(messageArraysList.size());
        for (Message message : messageArraysList) {
            out.println("To:" + message.getTo() + " Message:" + message.getMessage());
        }
    }

    private void help() throws SQLException {
        if(isAdmin(username)){
            out.println("Commands: send message, inbox, outbox, list users, add user, remove user, update password, update username, update address, log out, quit");
        }
        else{
            out.println("Commands: send message, inbox, outbox, update password, log out, quit");
        }
    }

    private boolean signIn() throws SQLException {
        boolean isAuthenticated = false;
        boolean isActive = false;
        try {
            username = in.readLine();
            String password = in.readLine();
            isAuthenticated = findUserByUsernameAndPassword(username, password);
            isActive = isActive(username);
            while (!isAuthenticated || isActive) {
                System.out.println("[SERVER] Authentication has failed");
                if(!isAuthenticated){
                    out.println("Wrong username or password");
                    username = in.readLine();
                    password = in.readLine();

                }
                else if(isActive){
                    System.out.println("[SERVER] Authentication has failed");
                    out.println("This user is active");
                    username = in.readLine();
                    password = in.readLine();

                }
                isAuthenticated = findUserByUsernameAndPassword(username, password);
                isActive = isActive(username);
            }
        } catch (IOException | SQLException e){
            System.err.println("IOException in ClientHandler");
        }

        System.out.println("[SERVER] Authentication is done");
        out.println("Authentication is done");

        return isAuthenticated;
    }
}

