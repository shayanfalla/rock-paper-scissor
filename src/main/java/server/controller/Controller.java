package server.controller;

import common.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.integration.PlayerDAO;
import server.model.Player;
import server.model.GameSession;

public class Controller extends UnicastRemoteObject implements Game {

    private final PlayerDAO playerDAO;
    private List<Player> Players;
    private int nrofplayers;
    private GameSession gamesession;

    public Controller() throws RemoteException {
        super();
        this.playerDAO = new PlayerDAO();
        this.nrofplayers = 0;
    }

    /*
    Initializes the gamesession
     */
    public void initGame() throws InterruptedException, RemoteException {
        gamesession = new GameSession(this);
        gamesession.start();
    }

    /*
    Return the number of players waiting to play or currently playing
     */
    public int getNrofplayers() {
        return nrofplayers;
    }

    /*
    Broadcasts a message to all players
     */
    public void broadmsg(String msg) {
        Players.forEach((p) -> {
            try {
                p.getPlayerObj().recvMsg(msg);
            } catch (RemoteException ex) {
            }
        });
    }

    /*
    Returns the player list from the database
     */
    public List<Player> getPlayers() {
        return Players = playerDAO.listPlayers();
    }

    /*
    Updates the score of all players
     */
    public void updateInfo(List<Player> players) throws RemoteException {
        for (Player p : players) {
            p.setMove("");
            playerDAO.updateInfo(p);
            p.getPlayerObj().recvMsg("Your total score is: " + p.getScore());
        }
    }

    /*
    Sets the clients username
     */
    @Override
    public void pickUsername(String username, ClientObject client) throws RemoteException {
        if (playerDAO.findPlayer(username) == null) {
            playerDAO.registerClient(new Player(username, client));
        } else {
            throw new RemoteException("Username '" + username + "' is in use. Pick a new username.");
        }
    }

    /*
    Deletes the client when the clientside has exited
     */
    @Override
    public void deletePlayer(String username) throws RemoteException {
        Player player = playerDAO.findPlayer(username);
        if (player != null) {
            playerDAO.deletePlayer(player);
            broadmsg(username + " has left the game. Restarting!");
            gamesession.terminate();
            try {
                initGame();
            } catch (InterruptedException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            getPlayers();
        } else {
            throw new RemoteException(username + " does not exist.");
        }
    }

    /*
    Passes on the move that the client made to the game session
    */
    @Override
    public void sendMove(String msg, String username) throws RemoteException {
        gamesession.playerMove(msg, username);
    }

    /*
    Used when the client enters PLAY, tells the server that the client is ready
    */
    @Override
    public void playGame() throws RemoteException {
        getPlayers();
        nrofplayers++;
    }

    /*
    Used when the client exits. Could be combined with deletePlayer
    */
    @Override
    public void leaveGame() throws RemoteException {
        nrofplayers--;
        if (nrofplayers < 0) {
            nrofplayers = 0;
        }
    }

    /*
    This determines if a session is ongoing if a client joins midsession.
    */
    @Override
    public boolean gameInSession() throws RemoteException {
        return gamesession.gameInSession();
    }
}
