package WhiteBoardInterface;

import Shapes.Shape;
import WhiteBoardClient.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface ClientUpdateRemote extends Remote {
    void clientGetCanvasUpdate(List<Shape> shapes) throws RemoteException;
    void clientGetChatUpdate(List<String> message) throws RemoteException;
    void clientGetUserListUpdate(ConcurrentHashMap<String, User> userList) throws RemoteException;
}