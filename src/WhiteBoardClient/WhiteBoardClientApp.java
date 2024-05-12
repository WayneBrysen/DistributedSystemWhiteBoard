package WhiteBoardClient;

import GUIComponents.DrawPanel;
import Shapes.Shape;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class WhiteBoardClientApp extends UnicastRemoteObject implements WhiteBoardInterface.ClientUpdateRemote {
    private DrawPanel drawPanel;

    public WhiteBoardClientApp(DrawPanel drawPanel) throws RemoteException {
        super();
        this.drawPanel = drawPanel;
    }

    public void clientGetCanvasUpdate(List<Shape> shapes) throws RemoteException {
        drawPanel.setShapes(shapes);
    }

    public void clientGetChatUpdate(String message) throws RemoteException {
}
