import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private int id;
    private boolean inGame;
    private int points;
    private int bet;
    private int chips;
    private boolean isfold;
    public String handStatus;
    public int handvalue;
    public boolean loss;
    private Deck hand;
   
   
    public boolean won;
    public Player(String name,int id,boolean inGame,Deck hand){
        this.name = name;
        this.id = id;
        this.inGame = inGame;
        points = 0;
        bet = 0;
        chips= 0;
        isfold = false;
        won = false;
        loss = false;
        handStatus = "";

        this.hand = hand;
        resetFold();

    }
    public void setName(String name){
        this.name = name;
    }
    public void setHand(Deck hand){
        this.hand = hand;

    }

    
    public Deck getHand(){
        return hand;
    }
    public void setFold(){
        isfold = true;
    }
    public void resetFold(){
        isfold = false;
    }
    public boolean getFold(){
        return isfold;
    }
    public void setChips(int val){
        chips = val;

    }
    public int getChips(){
        return chips;
    }
    public void setPoints(int val){
        points = val;
    }
    public int getPoints(){
        return points;
    }

    
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    
    public void setId(int id){
        this.id = id;
    }
   @Override
    public int hashCode() {
        return id;
    }
    public boolean isInGame(){
        return inGame;
    }
    public void setInGame(boolean inGame){
        this.inGame = inGame;

    }
    public int getBet(){
        return bet;
    }
    public void setBet(int val){
        bet = val;
    }
    
    
}
