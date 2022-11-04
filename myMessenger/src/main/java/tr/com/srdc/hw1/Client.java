package tr.com.srdc.hw1;

import java.io.*;
import java.net.Socket;

public class Client {

    private static final String IP = "127.0.0.1";
    private static final int PORT = 8090;

    public static void main(String[] args) throws IOException {
        Socket socket;
        String username;
        String password;
        String command;
        BufferedReader in;
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out;
        String response;
        String authenticationResponse;
        String protocol = "";

        socket = new Socket(IP, PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);


        while (true) {
            System.out.print("Enter your username: ");
            username = keyboard.readLine();
            out.println(username);
            System.out.print("Enter your password: ");
            password = keyboard.readLine();
            out.println(password);

            authenticationResponse = in.readLine();
            System.out.println(authenticationResponse);


            while (authenticationResponse.equals("Wrong username or password")
            || authenticationResponse.equals("This user is active")) {
                System.out.println(authenticationResponse);
                System.out.print("Enter your username: ");
                username = keyboard.readLine();
                out.println(username);
                System.out.print("Enter your password: ");
                password = keyboard.readLine();
                out.println(password);
                authenticationResponse = in.readLine();
            }

            System.out.print("\nLogged in\n");

            while (true) {
                System.out.print(">");
                command = keyboard.readLine();

                if (command.equals("quit")) {
                    socket.close();
                    System.exit(0);
                }
                else{
                    if(command.equals("send message")){
                        System.out.println("Send your message using the format: username of receiver-your message");
                        String message = keyboard.readLine();
                        protocol = "send message"+"-"+message;
                    }
                    else if(command.equals("inbox")){
                        protocol = "inbox";
                    }
                    else if(command.equals("outbox")){
                        protocol = "outbox";
                    }
                    else if(command.equals("add user")){
                        System.out.println("Enter the username of the new user");
                        String usernameInput = keyboard.readLine();
                        System.out.println("Enter the password of the new user");
                        String passwordInput = keyboard.readLine();
                        System.out.println("Enter the name of the new user");
                        String nameInput = keyboard.readLine();
                        System.out.println("Enter the surname of the new user");
                        String surnameInput = keyboard.readLine();
                        System.out.println("Enter the birth date of the new user (yyyy-mm-dd)");
                        String birthDateInput = keyboard.readLine();
                        System.out.println("Enter the gender of the new user");
                        String genderInput = keyboard.readLine();
                        System.out.println("Enter the email of the new user");
                        String emailInput = keyboard.readLine();
                        System.out.println("Enter the address of the new user");
                        String addressInput = keyboard.readLine();
                        System.out.println("Enter the user type of the new user (regular or admin)");
                        String userTypeInput = keyboard.readLine();
                        while(!(userTypeInput.equals("regular")||userTypeInput.equals("admin"))){
                            System.out.println("Enter regular or admin!");
                            System.out.println("Enter the user type of the new user");
                            userTypeInput = keyboard.readLine();
                        }
                        protocol = "add user"+"-"+usernameInput+"-"+passwordInput+"-"+nameInput
                                +"-"+surnameInput+"-"+birthDateInput+"-"+genderInput+"-"+emailInput+"-"+addressInput+"-"+userTypeInput;
                    }
                    else if(command.equals("remove user")){
                        System.out.println("Enter the username of the user you want to remove");
                        String usernameInput = keyboard.readLine();
                        protocol = "remove user"+"-"+usernameInput;
                    }
                    else if(command.equals("help")){
                        protocol = "help";
                    }
                    else if(command.equals("list users")){
                        protocol = "list users";
                    }
                    else if(command.equals("update password")){
                        System.out.println("Enter your new password");
                        String passwordInput = keyboard.readLine();
                        protocol = "update password"+"-"+passwordInput;
                    }
                    else if(command.equals("log out")){
                        protocol = "log out";
                    }
                    else if(command.equals("update username")){
                        System.out.println("Enter username of the user");
                        String usernameInput = keyboard.readLine();
                        System.out.println("Enter new username");
                        String newUsernameInput = keyboard.readLine();
                        protocol = "update username"+"-"+usernameInput+"-"+newUsernameInput;
                    }
                    else if(command.equals("update address")){
                        System.out.println("Enter username of the user");
                        String usernameInput = keyboard.readLine();
                        System.out.println("Enter new address");
                        String newAddressInput = keyboard.readLine();
                        protocol = "update address"+"-"+usernameInput+"-"+newAddressInput;
                    }
                    else{
                        protocol = " ";
                    }


                    out.println(protocol);
                    response = in.readLine();


                    if (response.isEmpty()) {
                        System.out.println("Error!!!");
                    } else if (response.equals("Send your message using the format: username of receiver-your message")) {
                        String isUserFound;
                        isUserFound = in.readLine();
                        if (isUserFound.equals("User found")) {
                            System.out.println("Message sent");
                        } else {
                            System.out.println(isUserFound);
                        }
                    } else if (response.equals("message box")) {
                        int size = Integer.parseInt(in.readLine());
                        int i = 0;
                        while (size > i) {
                            System.out.println(in.readLine());
                            i++;
                        }
                    } else if (response.equals("list users")) {
                        int size = Integer.parseInt(in.readLine());
                        int i = 0;
                        while (size > i) {
                            System.out.println(in.readLine());
                            i++;
                        }
                    } else if (response.equals("add user")) {
                        System.out.println("Name received");
                        System.out.println(in.readLine());
                    } else if (response.equals("remove user")) {
                        System.out.println(in.readLine());
                    } else if (response.equals("update password")) {
                        System.out.println("Password updated");
                    } else if (response.equals("update username")) {
                        System.out.println(in.readLine());
                    } else if (response.equals("update address")) {
                        System.out.println(in.readLine());
                    }else if(response.equals("log out")){
                        break;
                    }
                    else {
                        System.out.println(response);
                    }
                }
            }
        }
    }
}
