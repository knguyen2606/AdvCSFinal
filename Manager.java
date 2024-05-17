import java.util.ArrayList;

public class Manager {
    private ArrayList<ServerThread> stList;
    private  MyHashMap<Player,DLList<Player>> catchUp;
    private int idCatch;
    public Manager(){
        stList = new ArrayList<>();
        catchUp = new MyHashMap<>();

    }
    
    public void add(ServerThread st){
        stList.add(st);

    }
  
    public void brodcast(Object obj){
        for(int i = 0;i<stList.size();i++){
            System.out.println(obj+": obj sending");
          
            stList.get(i).send(obj);
            if(obj instanceof MyHashMap){
                   catchUp = (MyHashMap<Player,DLList<Player>>)obj;

            }
         
        }
    }

    public void setId(int id){
        idCatch= id;
    
    }
    public int getId(){
        return idCatch;
    }
    public  MyHashMap<Player,DLList<Player>> getMap(){
        return catchUp;
    }
   
}
