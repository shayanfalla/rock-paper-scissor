/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    HashMap players;

    public sessionGame(Controller controller) {
        this.controller = controller;
        this.wt = new WaitThread(controller);
        this.count = 0;
    }

    public void playerMove(String msg, String username) throws RemoteException {
        if (count == 1 && players.get(username).equals("")) {
            players.put(username, msg);
            controller.broadmsg(username + " has answered!");
        }
    }

    private int answers() {
        int answers = 0;
        Iterator it = players.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getValue().equals("")) {
                answers++;

            }
            it.remove();
        }
        return answers;
    }

    public void Gamesession() {
        int gamecounter = 0;
        List<Player> listPlayers;
        wt.start();
        while (true) {
            /*if(controller.getNrofplayers() > 0){
            controller.broadmsg("Waiting for " + (3 - controller.getNrofplayers()) + " players");
            }*/

            while (controller.getNrofplayers() >= 3) {
                if (count == 0) {
                    //(re)initialize players
                    wt.terminate();
                    players = new HashMap();
                    listPlayers = controller.getPlayers();
                    for (Player s : listPlayers) {
                        players.put(s.getUsername(), "");
                        System.out.println(s.getUsername());
                    }
                    count = 1;
                }
                if (count == 1) {
                    //all players give their moves
                    controller.broadmsg("Round " + gamecounter);
                    controller.broadmsg("Players! Enter your guesses");
                    while (answers() < 3) {
                       
                    }
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
