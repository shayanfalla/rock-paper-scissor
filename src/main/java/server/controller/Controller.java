package server.controller;

import common.Game;
import common.MessageToPlayers;
import common.PlayerDTO;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import server.integration.PlayerDAO;
import server.model.Player;

public class Controller extends UnicastRemoteObject implements Game {

    private final PlayerDAO playerDAO;
    
    public Controller() throws RemoteException {
        super();
        this.playerDAO = new PlayerDAO();
    }
    
    @Override
    public void pickUsername(String username) throws RemoteException {
        if (playerDAO.findPlayer(username) == null) {
            playerDAO.registerClient(new Player(username));
        } else {
            throw new RemoteException("Username '" + username + "' is in use. Pick a new username.");
        }
    }   

    @Override
    public PlayerDTO login(String username) throws RemoteException {
        Player player = playerDAO.findPlayer(username);
        if (player != null) {
            return player;
        } else {
            throw new RemoteException("Invalid username. Please, try again.");
        }
    }
    
    @Override
    public void deletePlayer(String username) throws RemoteException {
        Player player = playerDAO.findPlayer(username);
        if (player != null) {
            playerDAO.deletePlayer(player);
        } else {
            throw new RemoteException("Invalid credentials. Please, try again.");
        }
    }
    
    @Override
    public void playGame(PlayerDTO player, String move, MessageToPlayers msg) throws RemoteException {
        
    }
    
}
