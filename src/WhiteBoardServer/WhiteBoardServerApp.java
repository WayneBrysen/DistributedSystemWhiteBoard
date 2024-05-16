package WhiteBoardServer;

import Shapes.Shape;
import WhiteBoardClient.User;
import WhiteBoardInterface.ClientUpdateRemote;
import WhiteBoardInterface.WhiteBoardRemote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WhiteBoardServerApp extends UnicastRemoteObject implements WhiteBoardRemote {
    private List<ClientUpdateRemote> clients = new ArrayList<>();
    private List<Shape> shapes = new ArrayList<>();
    private List<String> messages = new ArrayList<>();
    private ConcurrentHashMap<String, User> userList = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, User> tempUserList = new ConcurrentHashMap<>();

    public WhiteBoardServerApp() throws RemoteException {
        super();
    }

    public void addManager(String username) throws RemoteException {
        userList.put(username, new User(username));
        updateUserListForAllClients();
    }

    public boolean addUser(String username) throws RemoteException {
        if (userList.containsKey(username) || tempUserList.containsKey(username)) {
            return false;
        } else {
            tempUserList.put(username, new User(username));
            updateTempUserListForManager();
            return  true;
        }
    }

    public void approveUser(String username) throws RemoteException {
        if (tempUserList.containsKey(username)) {
            userList.put(username, tempUserList.get(username));
            tempUserList.remove(username);
            updateTempUserListForManager();
            updateUserListForAllClients();
        }
    }

    public void denyUser(String username) throws RemoteException {
        if (tempUserList.containsKey(username)) {
            tempUserList.remove(username);
            updateTempUserListForManager();
        }
    }

    public void removeUser(String username) throws RemoteException {
        if (userList.containsKey(username)) {
            ClientUpdateRemote kickClient = null;
            for (ClientUpdateRemote client : clients) {
                try {
                    if (client.getClientUsername().equals(username)) {
                        kickClient = client;
                        break;
                    }
                }catch (RemoteException e) {
                    System.out.println("Error about get client's username: "+ e.getMessage());
                }
            }

            if (kickClient!= null && kickClient != clients.get(0)) {
                clients.remove(kickClient);
                try {
                    kickClient.clientKickedUpdate("You are removed by manager");
                } catch (RemoteException e)
                {
                    System.out.println("Error about kicking this client: " + e.getMessage());
                }
            }

            userList.remove(username);
            updateUserListForAllClients();
        }
    }



    public void updateTempUserListForManager() throws RemoteException {
        System.out.println("Manager approval requested.");
        clients.get(0).clientApprovalUpdate(new ConcurrentHashMap<>(tempUserList));
    }

    public ConcurrentHashMap<String, User> getUserList() throws RemoteException {
        return new ConcurrentHashMap<>(userList);
    }

    public ConcurrentHashMap<String, User> getTempUserList() throws RemoteException {
        return new ConcurrentHashMap<>(tempUserList);
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

    public void setShapes(List<Shape> shapes) throws RemoteException {
        this.shapes = new ArrayList<>(shapes);
        updateCanvasForAllClients();
    }

    public List<Shape> getShapes() throws RemoteException {
        return new ArrayList<>(shapes);
    }

    public void updateCanvasForAllClients() throws RemoteException {
        for (ClientUpdateRemote client : clients) {
            client.clientGetCanvasUpdate(new ArrayList<>(shapes));
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

    public void serverShutdownNotification() {
        for (ClientUpdateRemote client : clients) {
            try{
                client.serverShutDownUpdate("Manager leaved, this application is down now");
            } catch (RemoteException e) {
                System.out.println("Error to notify clients: "+ e.getMessage());
            }
        }
    }
}