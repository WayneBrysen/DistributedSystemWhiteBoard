package WhiteBoardClient;

import GUIComponents.ManagerApprovalPanel;
import GUIComponents.MenuPanel;
import GUIComponents.PeerAndChatPanel;
import GUIComponents.DrawPanel;
import Shapes.Shape;
import WhiteBoardInterface.ClientUpdateRemote;
import WhiteBoardInterface.WhiteBoardRemote;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WhiteBoardClient {
    private DrawPanel drawPanel;
    private WhiteBoardRemote serverAPP;
    private PeerAndChatPanel peerAndChatPanel;
    private ClientUpdateRemote clientApp;
    private ManagerApprovalPanel approvalPanel;
    private MenuPanel menuPanel;


    public WhiteBoardClient(String serverIPAddress, int serverPort, String username, boolean isManager) {

        if (!buildConnection(serverIPAddress, serverPort, username, isManager)) {
            usernameRepeatDialog(serverIPAddress, serverPort, isManager);
        } else {
            UISetup(username, isManager);
        }
    }

    private boolean buildConnection(String serverIPAddress, int serverPort, String username, boolean isManager) {

        try {
            Registry registry = LocateRegistry.getRegistry(serverIPAddress, serverPort);
            serverAPP = (WhiteBoardRemote) registry.lookup("WhiteBoardServer");

            boolean isInList = true;

            if (isManager) {
                serverAPP.addManager(username);
            } else {
                isInList = serverAPP.addUser(username);
            }

            if (!isInList) {
                System.out.println("username: " + username + " already exists on server");
                return false;
            }

            ConcurrentHashMap<String, User> userList = serverAPP.getUserList();
            ConcurrentHashMap<String, User> tempUserList = serverAPP.getTempUserList();

            if (!isManager) {
                while (!userList.containsKey(username)) {
                    tempUserList = serverAPP.getTempUserList();
                    Thread.sleep(2000);
                    if(!tempUserList.containsKey(username))
                    {
                        System.out.println("You have been denied by the manager.");
                        System.exit(0);
                    }
                    userList = serverAPP.getUserList();
                }
            }

            drawPanel = new DrawPanel(serverAPP);

            peerAndChatPanel = new PeerAndChatPanel(isManager, serverAPP, drawPanel, username);

            if (isManager) {
                approvalPanel = new ManagerApprovalPanel(serverAPP);
                menuPanel = new MenuPanel(drawPanel, serverAPP, username);
                clientApp = new WhiteBoardClientApp(drawPanel, peerAndChatPanel, approvalPanel, username, serverAPP);
            } else {
                clientApp = new WhiteBoardClientApp(drawPanel, peerAndChatPanel, null, username, serverAPP);
            }

            serverAPP.addNewClient(clientApp);

            List<Shape> canvasShapes = serverAPP.getShapes();
            List<String> chatHistory = serverAPP.getMessages();

            drawPanel.setShapes(canvasShapes);
            peerAndChatPanel.setMessages(chatHistory);
            peerAndChatPanel.setUserList(userList);

            if (isManager) {
                approvalPanel.updateTempUserList(tempUserList);
            }

            return true;

        } catch (Exception e) {
            System.err.println("Client initiate error: " + e.getMessage());
            System.exit(1);
            return  false;
        }
    }

    private void usernameRepeatDialog(String serverIPAddress, int serverPort, boolean isManager) {
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
                if (buildConnection(serverIPAddress, serverPort, textField.getText(), isManager)) {
                    frame.dispose();
                    UISetup(textField.getText(), isManager);
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

    private void UISetup(String username, boolean isManager) {
        JFrame frame = new JFrame("WhiteBoard Client - "+ username + (isManager? " (Manager)" : ""));
        frame.add(drawPanel, BorderLayout.CENTER);
        frame.add(peerAndChatPanel, BorderLayout.EAST);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        if (isManager) {
            frame.setSize(1200, 600);
        } else{
            frame.setSize(1000, 600);
        }
        frame.setVisible(true);

        if (isManager) {
            frame.add(menuPanel, BorderLayout.NORTH);
            frame.add(approvalPanel, BorderLayout.WEST);
        }

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    serverAPP.removeClient(clientApp);
                    serverAPP.removeUser(username);
                } catch (Exception error) {
                    System.out.print("Error while closing client: " + error.getMessage());
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

        new WhiteBoardClient(serverIPAddress, serverPort, username, false);
    }
}
