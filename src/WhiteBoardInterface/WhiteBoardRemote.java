package WhiteBoardInterface;

import Shapes.Shape;
import WhiteBoardClient.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface WhiteBoardRemote extends Remote {

    void addShape(Shape shape) throws RemoteException;
    List<Shape> getShapes() throws RemoteException;
    void setShapes(List<Shape> shapes) throws RemoteException;

    void addMessage(String message) throws RemoteException;
    List<String> getMessages() throws RemoteException;

    void updateCanvasForAllClients() throws RemoteException;

    void updateChatForAllClients() throws RemoteException;

    boolean addUser(String username) throws RemoteException;
    void addManager(String username) throws RemoteException;
    void removeUser(String username) throws RemoteException;
    void approveUser(String username) throws RemoteException;
    void denyUser(String username) throws RemoteException;
    ConcurrentHashMap<String, User> getTempUserList() throws RemoteException;
    ConcurrentHashMap<String, User> getUserList() throws RemoteException;
    void updateTempUserListForManager() throws RemoteException;

    void addNewClient(ClientUpdateRemote client) throws RemoteException;
    void removeClient(ClientUpdateRemote client) throws RemoteException;
}