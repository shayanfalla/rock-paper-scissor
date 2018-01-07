package server.model;

import static java.lang.Thread.sleep;
import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.Controller;

/*
The core of the server.
This is where the game is run.
 */
public class GameSession extends Thread {

    Controller controller;
    public WaitThread wt;
    private boolean count;
    private boolean game;
    private int answers;
    List<Player> listPlayers;

    public GameSession(Controller controller) {
        this.controller = controller;
        this.wt = new WaitThread(controller);
        this.count = false;
        this.answers = 0;
        this.listPlayers = controller.getPlayers();
    }

    /*
    Returns the player object of the specific username (client)
     */
    public Player getPlayer(String username) {
        if (count) {
            for (Player player : listPlayers) {
                if (player.getUsername().equals(username)) {
                    return player;
                }
            }
        }
        return null;
    }

    /*
    Sets the move that the client made
     */
    public Player setPlayer(String username, String move) {
        if (count) {
            for (int i = 0; i < listPlayers.size(); i++) {
                if (listPlayers.get(i).getUsername().equals(username)) {
                    listPlayers.get(i).setMove(move);
                    answers++;
                    controller.broadmsg(username + " has made their move!");
                }
            }
        }
        return null;
    }

    /*
    Checks if the player has already made their move. 
    If not(and it is valid), the move is registered
     */
    public void playerMove(String msg, String username) throws RemoteException {
        Player player = getPlayer(username);
        if (count) {
            if (player.getMove().equals("")) {
                setPlayer(username, msg);
            } else {
                player.getPlayerObj().recvMsg("You have already answered!");
            }
        } else {
            player.getPlayerObj().recvMsg("Round has not started yet!");
        }
    }

    /*
    Returns the current state of the game
    Either ongoing or waiting
     */
    public boolean gameInSession() {
        return count;
    }

    /*
    Terminates the game session if a client exits
    */
    public void terminate() {
        game = false;
        wt.terminate();
    }

    /*
    Runs endlessly until a client exits
    The inner loop starts as soon as there are two clients
    Clients who then join afterwards has to wait until the round has ended
    */
    @Override
    public void run() {
        game = true;
        int gamecounter = 1;
        wt.start();
        while (game) {
            try {
                sleep(200);
            } catch (InterruptedException ex) {
            }

            while (game && controller.getNrofplayers() >= 2) {

                if (!count) {
                    wt.terminate();
                    listPlayers = controller.getPlayers();
                    count = true;
                }
                if (count) {
                    //all players give their moves
                    controller.broadmsg("Round " + gamecounter);
                    controller.broadmsg("Players! Enter your guesses");
                    while (answers < controller.getNrofplayers()) {
                        try {
                            sleep(200);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(GameSession.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    gamecounter++;
                    answers = 0;
                    try {
                        new GameLogic().game(listPlayers);
                    } catch (RemoteException ex) {
                        Logger.getLogger(GameSession.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    count = false;
                }
            }
        }
    }
}
