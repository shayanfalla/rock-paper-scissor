package server.model;

import static java.lang.Thread.sleep;
import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.Controller;

public class sessionGame extends Thread{

    Controller controller;
    public WaitThread wt;
    private boolean count;
    private int answers;
    List<Player> listPlayers;

    public sessionGame(Controller controller) {
        this.controller = controller;
        this.wt = new WaitThread(controller);
        this.count = false;
        this.answers = 0;
        this.listPlayers = controller.getPlayers();
    }

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

    public boolean gameInSession() {
        return count;
    }
    
    private boolean game;
    public void terminate(){
        game = false;
        wt.terminate();
    }
    
    @Override
    public void run(){
        game = true;
        int gamecounter = 1;
        wt.start();
        while (game) {
            try {
                /*if(controller.getNrofplayers() > 0){
                controller.broadmsg("Waiting for " + (3 - controller.getNrofplayers()) + " players");
                }*/
                sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(sessionGame.class.getName()).log(Level.SEVERE, null, ex);
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
                            Logger.getLogger(sessionGame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    gamecounter++;
                    answers = 0;
                    try {
                        new GameLogic().game(listPlayers);
                    } catch (RemoteException ex) {
                        Logger.getLogger(sessionGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    count = false;
                }
            }
        }
    }
}
