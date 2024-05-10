package WhiteBoardServer;

import Shapes.Shape;
import WhiteBoardInterface.WhiteBoardRemote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class WhiteBoardServerApp extends UnicastRemoteObject implements WhiteBoardRemote {
    private List<Shape> shapes;

    public WhiteBoardServerApp() throws RemoteException {
        super();
        shapes = new java.util.ArrayList<>();
    }

    public void addShape(Shape shape) throws RemoteException {
        shapes.add(shape);
    }

    public List<Shape> getShapes() throws RemoteException {
        return new ArrayList<>(shapes);
    }

    public void


}