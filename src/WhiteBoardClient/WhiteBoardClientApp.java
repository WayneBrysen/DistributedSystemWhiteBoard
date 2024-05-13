package WhiteBoardClient;

import GUIComponents.ChatWindow;
import GUIComponents.DrawPanel;
import Shapes.Shape;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class WhiteBoardClientApp extends UnicastRemoteObject implements WhiteBoardInterface.ClientUpdateRemote {
    private DrawPanel drawPanel;
    private ChatWindow chatWindow;

    public WhiteBoardClientApp(DrawPanel drawPanel, ChatWindow chatWindow) throws RemoteException {
        super();
        this.drawPanel = drawPanel;
        this.chatWindow = chatWindow;
    }

    public void clientGetCanvasUpdate(List<Shape> shapes) throws RemoteException {
        drawPanel.setShapes(shapes);
    }

    public void clientGetChatUpdate(List<String> message) throws RemoteException {
        chatWindow.updateChat(message);
    }
}
