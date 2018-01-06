package server.model;

import java.util.List;

public class GameLogic {
    public void game(List<Player> players) {
        for(Player p1 : players) {
            String move1 = p1.getMove();
            
            for(Player p2 : players) {
                if(!p1.getUsername().equals(p2.getUsername())) {
                    String move2 = p2.getMove();
                    
                    if(move1.equals("PAPER")) {
                        p1.setScore(move2.equals("ROCK") ? 1 : 0);
                    }
                    if(move1.equals("ROCK")) {
                        p1.setScore(move2.equals("SCISSORS") ? 1 : 0);
                    }
                    if(move1.equals("SCISSORS")) {
                        p1.setScore(move2.equals("PAPER") ? 1 : 0);
                    }
                }
            }
        }
        for(Player p : players){
            System.out.println(p.getScore());
        }
    }
}
