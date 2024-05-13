package WhiteBoardClient;

import GUIComponents.PeerAndChatPanel;
import GUIComponents.DrawPanel;
import Shapes.Shape;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WhiteBoardClientApp extends UnicastRemoteObject implements WhiteBoardInterface.ClientUpdateRemote {
    private DrawPanel drawPanel;
    private PeerAndChatPanel peerAndChatPanel;

    public WhiteBoardClientApp(DrawPanel drawPanel, PeerAndChatPanel peerAndChatPanel) throws RemoteException {
        super();
        this.drawPanel = drawPanel;
        this.peerAndChatPanel = peerAndChatPanel;
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
}
