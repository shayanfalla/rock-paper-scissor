package server.model;

import common.MessageToPlayers;
import common.PlayerDTO;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity(name = "Player")
public class Player implements PlayerDTO, Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true, nullable = false)
    private String username; 
    
    @Column(nullable = false)
    private int score;
    
    @Column(nullable = false)
    private MessageToPlayers playerObj;
    
    @Column(nullable = false)
    private String move;
        
    public Player() {
    }
    
    public Player(String username, MessageToPlayers playerObj) {
        this.username = username;
        this.playerObj = playerObj;
        this.move = "";
    }
    
    public void setMove(String move){
        this.move = move;
    }
    
    public String getMove(){
        return this.move;
    }
    public void setPlayerObj(MessageToPlayers playerObj){
        this.playerObj = playerObj;
    }
    
    public MessageToPlayers getPlayerObj(){
        return this.playerObj;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

   /* public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }*/
}
