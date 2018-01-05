/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import java.util.HashMap;
import java.util.List;
import server.controller.Controller;

public class sessionGame {

    Controller controller;

    public sessionGame(Controller controller) {
        this.controller = controller;
    }

    public void Gamesession() {
        int count = 0;
        HashMap players;
        List<Player> listPlayers;
        while (true) {
            if(controller.getNrofplayers() > 0){
            controller.broadmsg("Waiting for " + (3 - controller.getNrofplayers()) + " players");
            }
            while (controller.getNrofplayers() >= 3) {
                if(count == 0){ 
                    //we reinitialize
                    players = new HashMap();
                    listPlayers = controller.getPlayers();
                    for(Player s : listPlayers){
                        players.put(s.getUsername(), "");
                        System.out.println(s.getUsername());
                    }
                    count = 1;
                }
                if(count == 1){  
                    controller.broadmsg("We're in 1");
                    count = 2;
                    //all players give their moves
                }
                if(count == 2){
                    //We send results
                }
            }
        }
    }
}
