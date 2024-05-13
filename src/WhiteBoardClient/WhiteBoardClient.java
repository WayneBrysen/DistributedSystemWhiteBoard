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
        buildConnection(serverIPAddress, serverPort, username);
        UISetup(username);

    }

    private void buildConnection(String serverIPAddress, int serverPort, String username) {
        try {
            Registry registry = LocateRegistry.getRegistry(serverIPAddress, serverPort);
            serverAPP = (WhiteBoardRemote) registry.lookup("WhiteBoardServer");

            if (serverAPP.addUser(username)) {
                System.out.println("using username: " + username + " to connect to server");
            } else {
                System.out.println("username: " + username + " already exists on server");
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
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Client initiate error: " + e.getMessage());
        }
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
