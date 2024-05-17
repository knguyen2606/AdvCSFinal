import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private int id;
    public Player(String name,int id){
        this.name = name;
        this.id = id;
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
}
