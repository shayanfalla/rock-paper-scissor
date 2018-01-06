package server.model;

import static java.lang.Thread.sleep;
import java.rmi.RemoteException;
import java.util.List;
import server.controller.Controller;

public class sessionGame {

    Controller controller;
    WaitThread wt;
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
                    System.out.println("found player! Player is: " + player.getUsername());
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
                    System.out.println("Setting move! Move is: " + move);
                    listPlayers.get(i).setMove(move);
                    System.out.println(listPlayers.get(i).getMove());
                    answers++;
                }
            }
        }
        return null;
    }

    public void playerMove(String msg, String username) throws RemoteException {
        Player player = getPlayer(username);
        if (count) {
            if (player.getMove().equals("")) {
                System.out.println("Setting move! Inside playerMove, move is " + msg);
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
    
    public void Gamesession() throws InterruptedException, RemoteException {
        int gamecounter = 1;
        wt.start();
        while (true) {
            /*if(controller.getNrofplayers() > 0){
            controller.broadmsg("Waiting for " + (3 - controller.getNrofplayers()) + " players");
            }*/
            sleep(200);

            while (controller.getNrofplayers() >= 2) {
                
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
                        sleep(200);
                    }
                    gamecounter++;
                    answers = 0;
                    new GameLogic().game(listPlayers);
                    count = false;
                }
            }
        }
    }
}
