package WhiteBoardServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class WhiteBoardServer {
    public static void main(String[] args) {
        try {
            WhiteBoardServerApp serverApp = new WhiteBoardServerApp();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("WhiteBoardServer", serverApp);

            System.out.println("Server is online");

        } catch (Exception e) {
            System.out.println("Server error occur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}