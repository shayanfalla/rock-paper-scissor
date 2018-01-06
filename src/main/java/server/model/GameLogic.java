package server.model;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.Controller;

public class GameLogic {

    public void game(List<Player> players) throws RemoteException {
        int roundscore;
        for (Player p1 : players) {
            String move1 = p1.getMove();
            roundscore = 0;
            for (Player p2 : players) {
                if (!p1.getUsername().equals(p2.getUsername())) {
                    String move2 = p2.getMove();

                    if (move1.equals("PAPER") && move2.equals("ROCK")) {
                        p1.setScore(1);
                        roundscore++;
                    }
                    if (move1.equals("ROCK") && move2.equals("SCISSOR")) {
                        p1.setScore(1);
                        roundscore++;
                    }
                    if (move1.equals("SCISSOR") && move2.equals("PAPER")) {
                        p1.setScore(1);
                        roundscore++;
                    }
                }
            }
            p1.getPlayerObj().recvMsg("Your score for this round was: " + roundscore);
        }
        new Controller().updateInfo(players);
    }
}
