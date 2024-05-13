package WhiteBoardServer;

import Shapes.Shape;
import WhiteBoardClient.User;
import WhiteBoardInterface.ClientUpdateRemote;
import WhiteBoardInterface.WhiteBoardRemote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WhiteBoardServerApp extends UnicastRemoteObject implements WhiteBoardRemote {
    private List<ClientUpdateRemote> clients = new ArrayList<>();
    private List<Shape> shapes = new ArrayList<>();
    private List<String> messages = new ArrayList<>();
    private ConcurrentHashMap<String, User> userList = new ConcurrentHashMap<>();

    public WhiteBoardServerApp() throws RemoteException {
        super();
    }

    public boolean addUser(String username) throws RemoteException {
        if (userList.containsKey(username)) {
            return false;
        } else {
            userList.put(username, new User(username));
            updateUserListForAllClients();
            return  true;
        }
    }

    public void kickUser(String username) throws RemoteException {
        if (userList.containsKey(username)) {
            userList.get(username).setKicked(true);
            userList.remove(username);
            updateUserListForAllClients();
        }
    }

    public ConcurrentHashMap<String, User> getUserList() throws RemoteException {
        return new ConcurrentHashMap<>(userList);
    }

    public void updateUserListForAllClients() throws RemoteException {
        for (ClientUpdateRemote client : clients) {
            client.clientGetUserListUpdate(new ConcurrentHashMap<>(userList));
        }
    }

    public void addShape(Shape shape) throws RemoteException {
        shapes.add(shape);
        updateCanvasForAllClients();
    }

    public List<Shape> getShapes() throws RemoteException {
        return new ArrayList<>(shapes);
    }

    public void updateCanvasForAllClients() throws RemoteException {
        for (ClientUpdateRemote client : clients) {
            client.clientGetCanvasUpdate(new ArrayList<>(shapes));
            client.clientGetChatUpdate(new ArrayList<>(messages));
        }
    }

    public void addMessage(String message) throws RemoteException {
        messages.add(message);
        updateChatForAllClients();
    }

    public List<String> getMessages() throws RemoteException {
        return new ArrayList<>(messages);
    }

    public void updateChatForAllClients() throws RemoteException {
        for (ClientUpdateRemote client : clients) {
            client.clientGetChatUpdate(new ArrayList<>(messages));
        }
    }

    public void addNewClient(ClientUpdateRemote client) throws RemoteException {
        clients.add(client);
        client.clientGetCanvasUpdate(new ArrayList<>(shapes));
        client.clientGetChatUpdate(new ArrayList<>(messages));
    }

    public void removeClient(ClientUpdateRemote client) throws RemoteException {
        clients.remove(client);
    }
}