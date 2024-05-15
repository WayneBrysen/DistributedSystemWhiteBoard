package WhiteBoardServer;

import WhiteBoardClient.WhiteBoardClient;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class WhiteBoardServer {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java CreateWhiteBoard  string <serverIPAddress> int <serverPort> string username");
            System.exit(1);
        }
        String serverIPAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String username = args[2];

        try {
            WhiteBoardServerApp serverApp = new WhiteBoardServerApp();

            Registry registry = LocateRegistry.createRegistry(serverPort);
            registry.rebind("WhiteBoardServer", serverApp);
            System.out.println("Server is ready at: "+ serverIPAddress + ":" + serverPort);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Server is shutting down");
                serverApp.serverShutdownNotification();
            }));

            WhiteBoardClient client = new WhiteBoardClient(serverIPAddress, serverPort, username, true);
            System.out.println("Greetings! Manager!");

        } catch (Exception e) {
            System.out.println("Error launching WhiteBoard: " + e.getMessage());
            e.printStackTrace();
        }
    }
}