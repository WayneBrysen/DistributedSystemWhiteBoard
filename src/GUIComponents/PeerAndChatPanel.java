package GUIComponents;
import java.awt.*;
import java.rmi.RemoteException;
import javax.swing.*;

import WhiteBoardClient.User;
import WhiteBoardInterface.WhiteBoardRemote;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PeerAndChatPanel extends JPanel{
    private JPanel chatWindow = new JPanel(new BorderLayout());

    private JList<String> userList;
    private DefaultListModel<String> userListModel = new DefaultListModel<>();

    private JTextArea displayArea = new JTextArea();
    private JTextField inputFiled = new JTextField();
    private WhiteBoardRemote serverAPP;
    private List<String> messages = new ArrayList<>();

    public PeerAndChatPanel(WhiteBoardRemote serverApp, DrawPanel drawPanel) {
        this.serverAPP = serverApp;
        setLayout(new BorderLayout());
        userListAdd();
        add(userList, BorderLayout.NORTH);

        displayChat();
        chatInput(drawPanel);
        add(chatWindow, BorderLayout.CENTER);
    }

    private void userListAdd() {
        userList = new JList<>(userListModel);
        userList.setBorder(BorderFactory.createTitledBorder("Online Users"));
        userList.setPreferredSize(new Dimension(200, 100));
    }

    private void displayChat() {
        displayArea.setEditable(false);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        chatWindow.add(scrollPane, BorderLayout.CENTER);
    }

    private void chatInput(DrawPanel drawPanel) {
        inputFiled.addActionListener(e -> {
            String message = inputFiled.getText();
            if (!message.isEmpty()) {
                try {
                    serverAPP.addMessage(message);
                    inputFiled.setText("");
                } catch (RemoteException error) {
                    drawPanel.addNotification("Error sending message, manager leaved");
                }
            }
        });
        chatWindow.add(inputFiled, BorderLayout.SOUTH);
    }

    public void updateDisplay() {
        displayArea.setText("");
        for (String message : messages) {
            displayArea.append(message + "\n");
        }
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
        updateDisplay();
    }

    public void setUserList(ConcurrentHashMap<String, User> userList) {
        userListModel.clear();
        for (String username : userList.keySet()) {
            userListModel.addElement(username);
        }
    }
}
