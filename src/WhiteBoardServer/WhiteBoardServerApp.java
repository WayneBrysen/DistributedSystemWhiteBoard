package WhiteBoardServer;

import Shapes.Shape;
import WhiteBoardInterface.ClientUpdateRemote;
import WhiteBoardInterface.WhiteBoardRemote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class WhiteBoardServerApp extends UnicastRemoteObject implements WhiteBoardRemote {
    private List<ClientUpdateRemote> clients = new ArrayList<>();
    private List<Shape> shapes = new ArrayList<>();
    private List<String> messages = new ArrayList<>();

    public WhiteBoardServerApp() throws RemoteException {
        super();
    }

    public void addShape(Shape shape) throws RemoteException {
        shapes.add(shape);
        updateCanvasForAllClients();
    }

    public List<Shape> getShapes() throws RemoteException {
        return new ArrayList<>(shapes);
    }

    public void addMessage(String message) throws RemoteException {
        messages.add(message);
        updateCanvasForAllClients();
    }

    public void updateChatForAllClients() throws RemoteException {
        for (ClientUpdateRemote client : clients) {
            client.clientGetChatUpdate(new ArrayList<>(messages));
        }
    }


    public void updateCanvasForAllClients() throws RemoteException {
        for (ClientUpdateRemote client : clients) {
            client.clientGetCanvasUpdate(new ArrayList<>(shapes));
        }
    }

    public void addNewClient(ClientUpdateRemote client) throws RemoteException {
        clients.add(client);
        client.clientGetCanvasUpdate(new ArrayList<>(shapes));
    }
}