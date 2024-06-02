import java.util.HashMap;
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
        int[] values = new int[hand.size()];
        for (int i = 0; i < hand.size(); i++) {
            int value = hand.getCard(i).getValue();
            values[i] = value == 1 ? 14 : value; // Ace can be high
        }

        sort(values);

        boolean isStraight = true;
        for (int i = 0; i < values.length - 1; i++) {
            if (values[i] + 1 != values[i + 1]) {
                isStraight = false;
                break;
            }
        }
        if (isStraight) {
            return "Straight";
        }

        // Check for Ace low straight (Ace-2-3-4-5)
        if (values[0] == 2 && values[1] == 3 && values[2] == 4 && values[3] == 5 && values[4] == 14) {
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

    public static int compareHands(PokerHandChecker hand1, PokerHandChecker hand2) {
        String[] handRankings = {
                "High Card", "One Pair", "Two Pair", "Three of a Kind", "Straight", "Flush", 
                "Full House", "Four of a Kind", "Straight Flush", "Royal Flush"
        };

        String handType1 = hand1.determineHand();
        String handType2 = hand2.determineHand();

        int rank1 = -1, rank2 = -1;
        for (int i = 0; i < handRankings.length; i++) {
            if (handRankings[i].equals(handType1)) {
                rank1 = i;
            }
            if (handRankings[i].equals(handType2)) {
                rank2 = i;
            }
        }

        if (rank1 != rank2) {
            return Integer.compare(rank1, rank2);
        } else {
            return compareSameTypeHands(hand1.hand, hand2.hand, handType1);
        }
    }

    private static int compareSameTypeHands(Deck hand1, Deck hand2, String handType) {
        int[] values1 = getSortedValues(hand1);
        int[] values2 = getSortedValues(hand2);

        switch (handType) {
            case "One Pair":
            case "Two Pair":
            case "Three of a Kind":
            case "Four of a Kind":
                return comparePairsOrMultiples(hand1, hand2);
            case "Full House":
                return compareFullHouse(hand1, hand2);
            case "Flush":
            case "Straight":
            case "Straight Flush":
            case "Royal Flush":
            case "High Card":
                return compareHighCard(values1, values2);
            default:
                return 0;
        }
    }

    private static int[] getSortedValues(Deck hand) {
        int[] values = new int[hand.size()];
        for (int i = 0; i < hand.size(); i++) {
            int value = hand.getCard(i).getValue();
            values[i] = value == 1 ? 14 : value; // Ace can be high
        }
        sort(values);
        return values;
    }

    private static void sort(int[] array) {
        // Simple bubble sort
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - 1 - i; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    private static int comparePairsOrMultiples(Deck hand1, Deck hand2) {
        Map<Integer, Integer> valueCount1 = new HashMap<>();
        Map<Integer, Integer> valueCount2 = new HashMap<>();

        for (int i = 0; i < hand1.size(); i++) {
            Card card = hand1.getCard(i);
            valueCount1.put(card.getValue(), valueCount1.getOrDefault(card.getValue(), 0) + 1);
        }
        for (int i = 0; i < hand2.size(); i++) {
            Card card = hand2.getCard(i);
            valueCount2.put(card.getValue(), valueCount2.getOrDefault(card.getValue(), 0) + 1);
        }

        int[] sorted1 = new int[valueCount1.size()];
        int[] sorted2 = new int[valueCount2.size()];

        int index = 0;
        for (Map.Entry<Integer, Integer> entry : valueCount1.entrySet()) {
            sorted1[index++] = entry.getKey();
        }
        index = 0;
        for (Map.Entry<Integer, Integer> entry : valueCount2.entrySet()) {
            sorted2[index++] = entry.getKey();
        }

        sort(sorted1);
        sort(sorted2);

        for (int i = sorted1.length - 1; i >= 0; i--) {
            if (sorted1[i] != sorted2[i]) {
                return sorted1[i] - sorted2[i];
            }
        }

        return 0;
    }

    private static int compareFullHouse(Deck hand1, Deck hand2) {
        Map<Integer, Integer> valueCount1 = new HashMap<>();
        Map<Integer, Integer> valueCount2 = new HashMap<>();

        for (int i = 0; i < hand1.size(); i++) {
            Card card = hand1.getCard(i);
            valueCount1.put(card.getValue(), valueCount1.getOrDefault(card.getValue(), 0) + 1);
        }
        for (int i = 0; i < hand2.size(); i++) {
            Card card = hand2.getCard(i);
            valueCount2.put(card.getValue(), valueCount2.getOrDefault(card.getValue(), 0) + 1);
        }

        int three1 = 0, two1 = 0, three2 = 0, two2 = 0;
        for (Map.Entry<Integer, Integer> entry : valueCount1.entrySet()) {
            if (entry.getValue() == 3) three1 = entry.getKey();
            if (entry.getValue() == 2) two1 = entry.getKey();
        }
        for (Map.Entry<Integer, Integer> entry : valueCount2.entrySet()) {
            if (entry.getValue() == 3) three2 = entry.getKey();
            if (entry.getValue() == 2) two2 = entry.getKey();
        }

        if (three1 != three2) return three1 - three2;
        return two1 - two2;
    }

    private static int compareHighCard(int[] values1, int[] values2) {
        for (int i = values1.length - 1; i >= 0; i--) {
            if (values1[i] != values2[i]) {
                return values1[i] - values2[i];
            }
        }
        return 0;
    }
}
