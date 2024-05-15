package WhiteBoardClient;

import GUIComponents.ManagerApprovalPanel;
import GUIComponents.PeerAndChatPanel;
import GUIComponents.DrawPanel;
import Shapes.Shape;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WhiteBoardClientApp extends UnicastRemoteObject implements WhiteBoardInterface.ClientUpdateRemote {
    private DrawPanel drawPanel;
    private PeerAndChatPanel peerAndChatPanel;
    private ManagerApprovalPanel managerApprovalPanel;
    private String username;

    public WhiteBoardClientApp(DrawPanel drawPanel, PeerAndChatPanel peerAndChatPanel, ManagerApprovalPanel managerApprovalPanel, String username) throws RemoteException {
        super();
        this.drawPanel = drawPanel;
        this.peerAndChatPanel = peerAndChatPanel;
        this.managerApprovalPanel = managerApprovalPanel;
        this.username = username;
    }

    public void clientGetUserListUpdate(ConcurrentHashMap<String, User> userList) throws RemoteException {
        peerAndChatPanel.setUserList(userList);
    }

    public void clientGetCanvasUpdate(List<Shape> shapes) throws RemoteException {
        drawPanel.setShapes(shapes);
    }

    public void clientGetChatUpdate(List<String> message) throws RemoteException {
        peerAndChatPanel.setMessages(message);
    }

    public void clientApprovalUpdate(ConcurrentHashMap<String, User> TempUserList) throws RemoteException {
        if (managerApprovalPanel != null) {
            System.out.println("Updating manager approval panel with new user list.");
            managerApprovalPanel.updateTempUserList(TempUserList);
        } else {
            System.out.println("Manager approval panel is null.");
        }
    }

    public void serverShutDownUpdate(String message) throws RemoteException {
        drawPanel.addNotification(message);
    }

    public String getClientUsername() throws RemoteException {
        return username;
    }

    public void clientKickedUpdate(String message) throws RemoteException {
        JOptionPane.showMessageDialog(null, message);
        System.exit(0);
    }
}
