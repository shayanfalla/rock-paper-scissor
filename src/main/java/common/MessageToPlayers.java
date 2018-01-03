package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageToPlayers extends Remote {
    
    void recvMsg(String msg) throws RemoteException;
}
