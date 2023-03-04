import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergii Bugaienko
 */

public class ChatServer {
    private static final Logger LOGGER  = LogManager.getLogger(ChatServer.class);
    private List<User> users;
    public static int SERVER_PORT = 512;

    public final static String EXIT_CMD = "/exit";
    public final static String RENAME_CMD = "/rn";
    public final static String USERS_SHOW_CMD = "/users";
    public final static String HELP_CMD = "/help";
    public final static String AUTH_CMD = "/auth";
    public final static String PASS_CHANGE_CMD = "/pass";
    public static String GET_NAME_CMD = "/clnm";

    private DbHandler dbHandler;

    private List<ClientHandler> clients;

    public static void main(String[] args) {
        new ChatServer();
    }

    public ChatServer() {
        int clientCount = 0;
        clients = new ArrayList<>();
        try {
            dbHandler = DbHandler.getInstance();
            users = dbHandler.getAllUsers();

//            for (User user : users) {
//                System.out.println(user);
//            }

            LOGGER.info("Connection to BD done");
//            System.out.println("Connection to BD done");
        } catch (SQLException e) {
            LOGGER.error(e);
//            e.printStackTrace();
        }
        LOGGER.info("Server started...");
//        System.out.println("Server started...");
        try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
            while (true) {
                Socket socket = server.accept();
                String name = "Client #" + (++clientCount);
                ClientHandler client = new ClientHandler(socket, name, dbHandler);
                clients.add(client);
                new Thread(client).start();
                LOGGER.info(name + ": joined.");
//                System.out.println(name + ": joined.");
            }
        } catch (IOException e) {
            LOGGER.error(e);
//            e.printStackTrace();
        }
    }

    private void sendToAll(String name, String message) {
        for (ClientHandler client : clients) {
            if (!name.equals(client.name)) {
                client.send(name + ": " + message);
            }
        }
    }

    private class ClientHandler implements Runnable {
        private BufferedReader reader;
        private PrintWriter writer;
        private Socket socket;
        private String name;
        private DbHandler dbHandler;
        private boolean isAuthorized;
        private User user;

        public ClientHandler(Socket socket, String name, DbHandler dbHandler) {
            this.socket = socket;
            this.name = name;
            this.dbHandler = dbHandler;
            isAuthorized = false;

//
//            dbHandler.addNewUser(name, "1234");
//            System.out.println("user create");
            users = dbHandler.getAllUsers();

//            for (User user : users) {
//                System.out.println(user);
//            }

            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                LOGGER.error(e);
//                e.printStackTrace();
            }
        }

        private boolean checkPass(String pass, User temp) {
            return temp.getPassword().equals(pass);
        }

        @Override
        public void run() {
            String message;
            try {
                do {
                    StringBuilder sb2 = new StringBuilder("Log in please.\n");
                    sb2.append("You should use the command -> " + AUTH_CMD + " name password\n");
                    send(sb2.toString());
                    message = reader.readLine();
                    String[] strings = message.split(" ");
                    if (strings.length >= 3 && strings[0].equals(AUTH_CMD)) {
                        String clientName = strings[1];
                        String clientPass = strings[2];
                        User tempUser = dbHandler.checkName(clientName);
                        if (tempUser == null) {
                            dbHandler.addNewUser(clientName, clientPass);
                            isAuthorized = true;
                            user = dbHandler.checkName(clientName);
                            users = dbHandler.getAllUsers();
                            send(clientName + ", you have successfully registered\n");

                        } else {
                            if (checkPass(clientPass, tempUser)) {
                                user = tempUser;
                                isAuthorized = true;
                                send(clientName + ", you have successfully logged in\n");
                            } else {
                                StringBuilder sb3 = new StringBuilder("User with the same name already exists.\n");
                                sb3.append("Please enter a valid password or select a different username\n");
                                send(sb3.toString());
                            }
                        }
                    }
                } while (!isAuthorized);
                name = user.getName();
                send(GET_NAME_CMD + " " + name);
                do {
                    message = reader.readLine();
                    if (isCommandInMessage(message).length == 0) {
                        sendToAll(name, message);
                    }
//                    System.out.println(name + ": " + message);
                } while (!message.equalsIgnoreCase(EXIT_CMD));

                socket.close();
            } catch (IOException e) {
                LOGGER.error(e);
//                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    LOGGER.error(e);
                }
            }
            clients.remove(this);
            LOGGER.info(name + ": disconnected.");
//            System.out.println(name + ": disconnected.");
        }

        private void send(String message) {
            writer.println(message);
            writer.flush();
        }

        private String[] isCommandInMessage(String message) {
            String[] messageWithoutNoCommand = new String[0];

            if (message.length() < 1) {
                return messageWithoutNoCommand;
            }
            String[] strings = message.split(" ");

            switch (strings[0]) {
                case EXIT_CMD:
                    sendToAll(name, "left the chat");
                    send(EXIT_CMD);
                    return strings;
                case RENAME_CMD:
                    if (strings[1] != null) {
                        name = strings[1];
                        Boolean isNameChanged = dbHandler.changeName(user.getId(), name);
                        send(isNameChanged? "You changed your name to: " + name : "Can't change name");
                        if (isNameChanged) sendToAll(name, "My new name: " + name);
                    }
                    return strings;
                case USERS_SHOW_CMD:
                    StringBuilder stringBuilder = new StringBuilder("Users online: \n");
                    int count = 0;
                    for (ClientHandler client : clients) {
                        stringBuilder.append(client.name);
                        stringBuilder.append("\n");
                        count++;
                    }
                    stringBuilder.append("Total: " + count);
                    send(stringBuilder.toString());
                    return strings;
                case HELP_CMD:
                    StringBuilder sB2 = new StringBuilder("Commands: \n");
                    sB2.append(HELP_CMD + " -> read help \n");
                    sB2.append(USERS_SHOW_CMD + " -> show online users \n");
                    sB2.append(RENAME_CMD + " newName -> change Name to newName \n");
                    sB2.append(PASS_CHANGE_CMD + " newPassword -> change password to newPassword \n");
                    sB2.append("/exit -> Exit");
                    send(sB2.toString());
                    return strings;
                case PASS_CHANGE_CMD:
                    String newPass = strings[1];
//                    System.out.println("newPass: " + newPass);
                    if (newPass != null) {
                        boolean isChanged = dbHandler.changePass(user.getId(), newPass);
                        send(isChanged ? "Password was changed" : "Can not change password");
                    }
                    return strings;

                default:
                    return messageWithoutNoCommand;

            }
        }

    }
}
