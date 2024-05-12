package WhiteBoardClient;

import GUIComponents.DrawPanel;
import Shapes.Shape;
import WhiteBoardInterface.ClientUpdateRemote;
import WhiteBoardInterface.WhiteBoardRemote;

import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class WhiteBoardClient {
    private DrawPanel drawPanel;
    private WhiteBoardRemote serverAPP;

    public WhiteBoardClient() {
        buildConnection();
        UISetup();

    }

    private void buildConnection() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            serverAPP = (WhiteBoardRemote) registry.lookup("WhiteBoardServer");
            drawPanel = new DrawPanel(serverAPP);
            ClientUpdateRemote clientApp = new WhiteBoardClientApp(drawPanel);
            serverAPP.addNewClient(clientApp);

            List<Shape> canvasShapes = serverAPP.getShapes();
            drawPanel.setShapes(canvasShapes);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Client initiate error: " + e.getMessage());
        }
    }

    private void UISetup() {
        JFrame frame = new JFrame("WhiteBoard Client");
        frame.add(drawPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new WhiteBoardClient();
    }
}
