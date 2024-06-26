package WhiteBoardInterface;

import GUIComponents.ManagerApprovalPanel;
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
    void clientApprovalUpdate(ConcurrentHashMap<String, User> tempUserList) throws RemoteException;
    void serverShutDownUpdate(String message) throws RemoteException;
    void clientKickedUpdate(String message) throws RemoteException;
    String getClientUsername() throws RemoteException;
}