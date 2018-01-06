package server.model;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import server.controller.Controller;

public class sessionGame {

    Controller controller;
    WaitThread wt;
    private int count;
    private int answers;
    List<Player> listPlayers;

    public sessionGame(Controller controller) {
        this.controller = controller;
        this.wt = new WaitThread(controller);
        this.count = 0;
        this.answers = 0;
        this.listPlayers = controller.getPlayers();
    }

    public Player getPlayer(String username) {
        if (count == 1) {
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
        if (count == 1) {
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
        if (count == 1) {
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

    public void Gamesession() {
        int gamecounter = 1;
        wt.start();
        while (true) {
            /*if(controller.getNrofplayers() > 0){
            controller.broadmsg("Waiting for " + (3 - controller.getNrofplayers()) + " players");
            }*/

            while (controller.getNrofplayers() >= 3) {
                if (count == 0) {
                    //(re)initialize players
                    wt.terminate();
                    listPlayers = controller.getPlayers();
                    count = 1;
                }
                if (count == 1) {
                    //all players give their moves
                    controller.broadmsg("Round " + gamecounter);
                    controller.broadmsg("Players! Enter your guesses");
                    while (answers < 3) {

                    }
                    answers = 0;
                    count = 2;
                }
                if (count == 2) {
                    count = 1;
                    //We send results
                }
            }
        }
    }
}
