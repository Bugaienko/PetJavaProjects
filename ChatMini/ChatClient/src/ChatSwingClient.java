import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author Sergii Bugaienko
 */

public class ChatSwingClient extends JFrame {
    JTextField msgField;
    JTextArea dialogue;
    JButton send;
    JButton connect;
    PrintWriter writer;
    BufferedReader reader;
    Boolean isConnectServer = false;
//    private final static String SERVER_IP = "localhost";
    private final static String SERVER_IP = "135.125.151.98";
    private final static int SERVER_PORT = 512;
    public static String EXIT_CMD = "/exit";
    public static String GET_NAME_CMD = "/clnm";
    public static String RENAME_CMD = "/rn";


    public static void main(String[] args) {
        new ChatSwingClient();
    }

    public ChatSwingClient() {
        setTitle("Network chat client");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (writer != null) {
                    writer.println(EXIT_CMD);
                    writer.close();
                }
                super.windowClosing(e);
            }
        });

        dialogue = new JTextArea();
        dialogue.setLineWrap(true);
        dialogue.setEditable(false);
        JScrollPane scroll = new JScrollPane(dialogue);


        msgField = new JTextField();
        send = new JButton("Send");

        connect = new JButton("Connect");


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(connect);
        panel.add(msgField);
        panel.add(send);


        // Listen Enter key on Message Field
        msgField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessageToServer();
                }
                super.keyPressed(e);
            }
        });

        connect.addActionListener(e -> {
            if (isConnectServer) {
                disconnect();
            } else {
                connect();
            }
        });

        send.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO вынести в отдельный метод
                sendMessageToServer();
            }
        }));

        add(BorderLayout.CENTER, scroll);
        add(BorderLayout.SOUTH, panel);

        dialogue.setForeground(Color.BLUE);
        Font font = new Font("Verdana", Font.BOLD, 12);
        dialogue.setFont(font);

        setVisible(true);


        connect();

    }

    private void sendMessageToServer() {
        try {
            String echo = "";
            writer.println(msgField.getText());
            writer.flush();
            dialogue.append("   I:  " + msgField.getText() + "\n");

            msgField.setText("");
            msgField.requestFocusInWindow();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private class CommandHandler implements Runnable {
        private BufferedReader reader;

        public CommandHandler(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public void run() {
            String message;
            try {
                do {
                    message = reader.readLine();

                    if (message.equals(EXIT_CMD)) {
                        dialogue.append("   S: disconnect done" + "\n");
                        switchOfButtons();
                     } else if (message.contains(GET_NAME_CMD) && message.indexOf(GET_NAME_CMD) == 0) {
                        System.out.println("Надо бы имя поменять");
                        String[] mes = message.split(" ");
                        if (mes[1].length() > 0) {
                            setTitle("Network chat client :: " + mes[1]);
                        }
                    } else {
                        dialogue.append("   " + message + "\n");
                    }
                    System.out.println(message);

                } while (!message.equalsIgnoreCase("/exit"));
                System.out.println("connection close");
            } catch (SocketException se) {
                System.out.println("Connection to server lost");
                connect.setForeground(new Color(0, 100, 0));
                connect.setText("Connect");
                isConnectServer = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void disconnect() {
        String echo = "";
        writer.println(EXIT_CMD);
        writer.flush();
        dialogue.append("   S:  connection to server los" + "\n");

        switchOfButtons();

    }

    private void switchOfButtons() {
        connect.setForeground(new Color(0, 100, 0));
        dialogue.setBackground(new Color(253, 239, 255));
        connect.setText("Connect");
        isConnectServer = false;
        msgField.setEnabled(false);
        send.setEnabled(false);
    }

    private void connect() {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            connect.setForeground(Color.RED);
            dialogue.setBackground(new Color(224, 255, 224));
            connect.setText("Disconnect");
            isConnectServer = true;
            dialogue.append("   S:  connection to server -> successfully. For help sens /help" + "\n");
            msgField.setEnabled(true);
            send.setEnabled(true);
            msgField.requestFocusInWindow();
            System.out.println("Connect to server...");
            new Thread(new CommandHandler(reader)).start();
        } catch (IOException e) {
            isConnectServer = false;
            System.out.println("Can't Connect to server...");
        }


    }
}
