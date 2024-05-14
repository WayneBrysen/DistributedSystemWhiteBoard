package WhiteBoardClient;

import GUIComponents.PeerAndChatPanel;
import GUIComponents.DrawPanel;
import Shapes.Shape;
import WhiteBoardInterface.ClientUpdateRemote;
import WhiteBoardInterface.WhiteBoardRemote;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WhiteBoardClient {
    private DrawPanel drawPanel;
    private WhiteBoardRemote serverAPP;
    private PeerAndChatPanel peerAndChatPanel;
    private ClientUpdateRemote clientApp;


    public WhiteBoardClient(String serverIPAddress, int serverPort, String username) {
        if (!buildConnection(serverIPAddress, serverPort, username)) {
            usernameRepeatDialog(serverIPAddress, serverPort, username);
        } else {
            UISetup(username);
        }
    }

    private boolean buildConnection(String serverIPAddress, int serverPort, String username) {
        try {
            Registry registry = LocateRegistry.getRegistry(serverIPAddress, serverPort);
            serverAPP = (WhiteBoardRemote) registry.lookup("WhiteBoardServer");

            if (!serverAPP.addUser(username)) {
                System.out.println("username: " + username + " already exists on server");
                return false;
            }

            drawPanel = new DrawPanel(serverAPP);
            peerAndChatPanel = new PeerAndChatPanel(serverAPP);
            clientApp = new WhiteBoardClientApp(drawPanel, peerAndChatPanel);

            serverAPP.addNewClient(clientApp);

            List<Shape> canvasShapes = serverAPP.getShapes();

            List<String> chatHistory = serverAPP.getMessages();

            ConcurrentHashMap<String, User> userList = serverAPP.getUserList();

            drawPanel.setShapes(canvasShapes);
            peerAndChatPanel.setMessages(chatHistory);
            peerAndChatPanel.setUserList(userList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Client initiate error: " + e.getMessage());
            return false;
        }
    }

    private void usernameRepeatDialog(String serverIPAddress, int serverPort,String username) {
        JFrame frame = new JFrame("Enter a new username");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(310, 120);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        JTextField textField = new JTextField(20);
        JLabel prompt = new JLabel("Username already exists, Enter a new username:");
        prompt.setForeground(Color.BLACK);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                if (buildConnection(serverIPAddress, serverPort, textField.getText())) {
                    frame.dispose();
                    UISetup(textField.getText());
                } else {
                    prompt.setText("Username already exists, Enter a new username: ");
                    prompt.setForeground(Color.RED);
                    textField.requestFocusInWindow();
                }
            } catch (Exception error) {
                error.printStackTrace();
            }
        });

        panel.add(prompt);
        panel.add(textField);
        panel.add(submitButton);
        frame.getContentPane().removeAll();
        frame.add(panel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void UISetup(String username) {
        JFrame frame = new JFrame("WhiteBoard Client");
        frame.add(drawPanel, BorderLayout.CENTER);
        frame.add(peerAndChatPanel, BorderLayout.EAST);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    serverAPP.removeClient(clientApp);
                    serverAPP.kickUser(username);
                } catch (Exception error) {
                    error.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: java JoinWhiteBoard string <serverIPAddress> int <serverPort> string username");
            System.exit(1);
        }

        String serverIPAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String username = args[2];

        new WhiteBoardClient(serverIPAddress, serverPort, username);
    }
}
