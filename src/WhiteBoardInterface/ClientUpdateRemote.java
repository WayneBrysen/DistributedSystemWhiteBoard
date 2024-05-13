package WhiteBoardInterface;

import Shapes.Shape;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientUpdateRemote extends Remote {
    void clientGetCanvasUpdate(List<Shape> shapes) throws RemoteException;
    void clientGetChatUpdate(List<String> message) throws RemoteException;
}