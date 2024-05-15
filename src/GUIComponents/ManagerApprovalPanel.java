package GUIComponents;

import WhiteBoardClient.User;
import WhiteBoardInterface.WhiteBoardRemote;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;

public class ManagerApprovalPanel extends JPanel{
    private DefaultListModel<String> userListModel = new DefaultListModel<>();
    private JList<String> tempUserList;
    private JPanel buttonPanel = new JPanel();

    public ManagerApprovalPanel(WhiteBoardRemote serverAPP) {
        setLayout(new BorderLayout());
        tempUserListAdd();

        add(tempUserList, BorderLayout.NORTH);

        buttonPanel(buttonPanel, serverAPP);

        add(buttonPanel, BorderLayout.CENTER);
    }

    private void buttonPanel(JPanel buttonPanel, WhiteBoardRemote serverAPP) {
        JButton approveButton = new JButton("Approve");
        approveButton.addActionListener( e -> {
            String userSelection = tempUserList.getSelectedValue();
            try {
                serverAPP.approveUser(userSelection);
                userListModel.removeElement(userSelection);
            } catch (Exception error) {
                error.printStackTrace();
                System.out.println("Error approving user: " + userSelection);
            }
        });

        JButton denyButton = new JButton("Deny");

        denyButton.addActionListener( e ->  {
            String userSelection = tempUserList.getSelectedValue();
            try {
                serverAPP.denyUser(userSelection);
                userListModel.removeElement(userSelection);
            } catch (Exception error) {
                error.printStackTrace();
                System.out.println("Error denying user: " + userSelection);
            }
        });

        buttonPanel.add(approveButton);
        buttonPanel.add(denyButton);
    }

    private void tempUserListAdd() {
        tempUserList = new JList<>(userListModel);
        tempUserList.setBorder(BorderFactory.createTitledBorder("Pending User List"));
        tempUserList.setPreferredSize(new Dimension(200, 100));
    }

    public void updateTempUserList(ConcurrentHashMap<String, User> tempUserList) {
        userListModel.clear();
        for (String username : tempUserList.keySet()) {
            System.out.println("Adding user to list model: " + username);
            userListModel.addElement(username);
            System.out.println("Number of users after adding: " + userListModel.get(0));
            System.out.println("Successfully updated user list model." + tempUserList.get(username).getUsername());
        }
        System.out.println("Number of users after adding: " + userListModel.getSize());
    }
}


