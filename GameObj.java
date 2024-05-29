public class GameObj {

    private int players;
    private Deck hand;
    private DLList<Card> board;
    private int[][] stacks;
    private String winner;

    public GameObj(int players) {
        this.players = players;

        stacks = new int[players][3];

    }

    
    
    
}
