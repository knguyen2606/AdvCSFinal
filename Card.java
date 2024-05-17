public class Card {
    private char suit;
    private char value;

    public Card(char value, char suit) {
        this.value = value;
        this.suit = suit;
    }

    public char getSuit() {
        return suit;
    }

    public char getValue() {
        return value;
    }
}
