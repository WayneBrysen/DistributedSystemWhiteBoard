package WhiteBoardInterface;

import Shapes.Shape;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientUpdateRemote extends Remote {
    void clientGetUpdate (List<Shape> shapes) throws RemoteException;
}