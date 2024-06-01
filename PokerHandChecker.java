import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class PokerHandChecker {

    private Deck hand;

    public PokerHandChecker(Deck hand) {
        this.hand = hand;
    }

    public String determineHand() {
        String result;
        if ((result = isRoyalFlush(hand)) != null) return result;
        if ((result = isStraightFlush(hand)) != null) return result;
        if ((result = isFourOfAKind(hand)) != null) return result;
        if ((result = isFullHouse(hand)) != null) return result;
        if ((result = isFlush(hand)) != null) return result;
        if ((result = isStraight(hand)) != null) return result;
        if ((result = isThreeOfAKind(hand)) != null) return result;
        if ((result = isTwoPair(hand)) != null) return result;
        if ((result = isOnePair(hand)) != null) return result;
        return "High Card";
    }

    private static String isRoyalFlush(Deck hand) {
        if (isStraightFlush(hand) != null) {
            for (int i = 0; i < hand.size(); i++) {
                if (hand.getCard(i).getValue() == 1) {
                    return "Royal Flush";
                }
            }
        }
        return null;
    }

    private static String isStraightFlush(Deck hand) {
        if (isFlush(hand) != null && isStraight(hand) != null) {
            return "Straight Flush";
        }
        return null;
    }

    private static String isFourOfAKind(Deck hand) {
        Map<Integer, Integer> valueCount = new HashMap<>();
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.getCard(i);
            valueCount.put(card.getValue(), valueCount.getOrDefault(card.getValue(), 0) + 1);
        }
        for (Map.Entry<Integer, Integer> entry : valueCount.entrySet()) {
            if (entry.getValue() == 4) {
                return "Four of a Kind";
            }
        }
        return null;
    }

    private static String isFullHouse(Deck hand) {
        Map<Integer, Integer> valueCount = new HashMap<>();
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.getCard(i);
            valueCount.put(card.getValue(), valueCount.getOrDefault(card.getValue(), 0) + 1);
        }
        boolean hasThree = false;
        boolean hasTwo = false;
        for (Map.Entry<Integer, Integer> entry : valueCount.entrySet()) {
            if (entry.getValue() == 3) {
                hasThree = true;
            }
            if (entry.getValue() == 2) {
                hasTwo = true;
            }
        }
        if (hasThree && hasTwo) {
            return "Full House";
        }
        return null;
    }

    private static String isFlush(Deck hand) {
        String suit = hand.getCard(0).getSuit();
        for (int i = 1; i < hand.size(); i++) {
            if (!hand.getCard(i).getSuit().equals(suit)) return null;
        }
        return "Flush";
    }

    private static String isStraight(Deck hand) {
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < hand.size(); i++) {
            int value = hand.getCard(i).getValue();
            values.add(value == 1 ? 14 : value); // Ace can be high
        }
        Collections.sort(values);
        boolean isStraight = true;
        for (int i = 0; i < values.size() - 1; i++) {
            if (values.get(i) + 1 != values.get(i + 1)) {
                isStraight = false;
                break;
            }
        }
        if (isStraight) {
            return "Straight";
        }

        // Check for Ace low straight (Ace-2-3-4-5)
        if (values.equals(List.of(2, 3, 4, 5, 14))) {
            return "Straight";
        }
        return null;
    }

    private static String isThreeOfAKind(Deck hand) {
        Map<Integer, Integer> valueCount = new HashMap<>();
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.getCard(i);
            valueCount.put(card.getValue(), valueCount.getOrDefault(card.getValue(), 0) + 1);
        }
        for (Map.Entry<Integer, Integer> entry : valueCount.entrySet()) {
            if (entry.getValue() == 3) {
                return "Three of a Kind";
            }
        }
        return null;
    }

    private static String isTwoPair(Deck hand) {
        Map<Integer, Integer> valueCount = new HashMap<>();
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.getCard(i);
            valueCount.put(card.getValue(), valueCount.getOrDefault(card.getValue(), 0) + 1);
        }
        int pairCount = 0;
        for (Map.Entry<Integer, Integer> entry : valueCount.entrySet()) {
            if (entry.getValue() == 2) {
                pairCount++;
            }
        }
        if (pairCount == 2) {
            return "Two Pair";
        }
        return null;
    }

    private static String isOnePair(Deck hand) {
        Map<Integer, Integer> valueCount = new HashMap<>();
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.getCard(i);
            valueCount.put(card.getValue(), valueCount.getOrDefault(card.getValue(), 0) + 1);
        }
        for (Map.Entry<Integer, Integer> entry : valueCount.entrySet()) {
            if (entry.getValue() == 2) {
                return "One Pair";
            }
        }
        return null;
    }
    
}
