import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private int id;
    private boolean inGame;
    private int points;
    public Player(String name,int id,boolean inGame){
        this.name = name;
        this.id = id;
        this.inGame = inGame;
        points = 0;
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
    
    
}
