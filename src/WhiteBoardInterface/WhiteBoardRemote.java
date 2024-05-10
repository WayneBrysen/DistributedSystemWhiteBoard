package WhiteBoardInterface;

import Shapes.Shape;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface WhiteBoardRemote extends Remote {

    void addShape(Shape shape) throws RemoteException;

    List<Shape> getShapes() throws RemoteException;
}