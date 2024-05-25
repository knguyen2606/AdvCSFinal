
import java.io.Serializable;
import java.util.Random;

public class Card implements Serializable{
    private String suit;
    private int value;
    private String imagePath;

    public Card(int value, String suit, String imagePath) {
        this.value = value;
        this.suit = suit;
        this.imagePath = imagePath;
        
    }

    public String getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }
    public String getImage(){
        return imagePath;
    }
}