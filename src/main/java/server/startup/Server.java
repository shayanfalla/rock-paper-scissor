package server.startup;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import server.controller.Controller;

public class Server {

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.startRMIServant();
            System.out.println("Game has started.");
        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Failed to start game server!");
            e.printStackTrace();
        }
    }

    private void startRMIServant() throws RemoteException, MalformedURLException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (RemoteException noRegistryRunning) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
        Controller controller = new Controller();
        Naming.rebind("gameproj", controller);
    }
    
}
