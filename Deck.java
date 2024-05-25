
import java.io.Serializable;
import java.util.Random;
public class Deck implements Serializable {

    private DLList<Card> deck;
    
    public Deck(DLList<Card> deck){
        this.deck = deck;
    }
    public int size(){
        return deck.size();
    }

   public void randomize() {
        Random random = new Random();
        for (int i = deck.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            swap(i, j);
        }
    }

    private void swap(int i, int j) {
        Card temp = deck.get(i);
        deck.set(i, deck.get(j));
        deck.set(j, temp);
    }
    public Card getCard(int index){
        return deck.get(index);

    }
    public DLList<Card> getDeck(){
        return deck;
    }
    public void remove(int index){
        deck.remove(index);
    }
}


