package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Game extends Remote {
    
    final String SERVER_NAME_IN_REGISTRY = "gameproj";
    
    void pickUsername(String username, MessageToPlayers client) throws RemoteException;
    
    PlayerDTO login(String username) throws RemoteException;
    
    void deletePlayer(String username) throws RemoteException;
    
    void sendMove(String msg, String username) throws RemoteException;
    
    void playGame() throws RemoteException;
    
    void leaveGame() throws RemoteException;
    
    boolean gameInSession() throws RemoteException;
}
