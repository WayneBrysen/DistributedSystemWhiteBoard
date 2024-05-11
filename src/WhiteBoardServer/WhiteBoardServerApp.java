package WhiteBoardServer;

import Shapes.Shape;
import WhiteBoardClient.WhiteBoardClientApp;
import WhiteBoardInterface.WhiteBoardRemote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class WhiteBoardServerApp extends UnicastRemoteObject implements WhiteBoardRemote {
    private List<WhiteBoardClientApp> clients = new ArrayList<>();
    private List<Shape> shapes = new ArrayList<>();

    public WhiteBoardServerApp() throws RemoteException {
        super();
    }

    public void addShape(Shape shape) throws RemoteException {
        shapes.add(shape);

    }

    public List<Shape> getShapes() throws RemoteException {
        return new ArrayList<>(shapes);
    }

    public void updateAllClients() throws RemoteException {
        for (WhiteBoardClientApp client : clients) {
            client.clientGetUpdate(new ArrayList<>(shapes));
        }
    }

    public void addNewClient(WhiteBoardClientApp client) throws RemoteException {
        clients.add(client);
        client.clientGetUpdate(new ArrayList<>(shapes));
    }


}