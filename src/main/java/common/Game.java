package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Game extends Remote {
    
    final String SERVER_NAME_IN_REGISTRY = "gameproj";
    
    void pickUsername(String username) throws RemoteException;
    
    void leaveGame(String username) throws RemoteException;
    
    void compareGuess(String guess) throws RemoteException;
}
