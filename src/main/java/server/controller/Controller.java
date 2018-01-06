package server.controller;

import common.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.integration.PlayerDAO;
import server.model.Player;
import server.model.sessionGame;

public class Controller extends UnicastRemoteObject implements Game {

    private final PlayerDAO playerDAO;
    private List<Player> Players;
    private int nrofplayers;
    private sessionGame gamesession;

    public Controller() throws RemoteException {
        super();
        this.playerDAO = new PlayerDAO();
        this.nrofplayers = 0;
    }

    public void initGame() throws InterruptedException, RemoteException {
        gamesession = new sessionGame(this);
        gamesession.Gamesession();
    }

    public int getNrofplayers() {
        return nrofplayers;
    }

    public void broadmsg(String msg) {
        Players.forEach((p) -> {
            try {
                p.getPlayerObj().recvMsg(msg);
            } catch (RemoteException ex) {
            }
        });
    }

    public List<Player> getPlayers() {
        return Players = playerDAO.listPlayers();
    }

    public void updateInfo(List<Player> players) {
        for (Player p : players) {
            p.setMove("");
            playerDAO.updateInfo(p);
            broadmsg("Your total score is: " + p.getScore());
        }
    }

    @Override
    public void pickUsername(String username, MessageToPlayers client) throws RemoteException {
        if (playerDAO.findPlayer(username) == null) {
            playerDAO.registerClient(new Player(username, client));
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
            getPlayers();
        } else {
            throw new RemoteException(username + " does not exist.");
        }
    }

    @Override
    public void sendMove(String msg, String username) throws RemoteException {
        gamesession.playerMove(msg, username);
    }

    @Override
    public void playGame() throws RemoteException {
        getPlayers();
        nrofplayers++;
    }

    @Override
    public void leaveGame() throws RemoteException {
        nrofplayers--;
        if (nrofplayers < 0) {
            nrofplayers = 0;
        }
    }

    @Override
    public boolean gameInSession() throws RemoteException {
        return gamesession.gameInSession();
    }
}
