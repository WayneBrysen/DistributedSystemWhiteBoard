package WhiteBoardClient;

import Shapes.Shape;
import WhiteBoardInterface.ClientUpdateRemote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class WhiteBoardClientApp extends UnicastRemoteObject implements ClientUpdateRemote {
    private DrawPanel drawPanel;

    public WhiteBoardClientApp(DrawPanel drawPanel) throws Exception {
        super();
        this.drawPanel = drawPanel;
    }

    public void clientGetUpdate(List<Shape> shapes) throws RemoteException {
        drawPanel.setShapes(shapes);
    }
}
