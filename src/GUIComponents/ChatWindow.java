package GUIComponents;
import java.awt.*;
import java.rmi.RemoteException;
import javax.swing.*;
import WhiteBoardInterface.WhiteBoardRemote;
import java.util.List;
public class ChatWindow extends JPanel{
    private JPanel chatWindow = new JPanel(new BorderLayout());
    private JTextArea displayArea = new JTextArea();
    private JTextField inputFiled = new JTextField();
    private WhiteBoardRemote serverAPP;

    public ChatWindow(WhiteBoardRemote serverApp) {
        this.serverAPP = serverApp;
        setLayout(new BorderLayout());
        displayChat();
        chatInput();
        add(chatWindow, BorderLayout.CENTER);
    }

    private void displayChat() {
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        chatWindow.add(scrollPane, BorderLayout.CENTER);
    }

    private void chatInput() {
        inputFiled.addActionListener(e -> {
            String message = inputFiled.getText();
            if (!message.isEmpty()) {
                try {
                    serverAPP.addMessage(message);
                    inputFiled.setText("");
                } catch (RemoteException error) {
                    error.printStackTrace();
                }
            }
        });
        chatWindow.add(inputFiled, BorderLayout.SOUTH);
    }

    public void updateChat(List<String> messages) {
        displayArea.setText("");
        for (String message : messages) {
            displayArea.append(message + "\n");
        }

    }
}
