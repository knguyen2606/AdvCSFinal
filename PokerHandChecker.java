import java.util.*;

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
        Map<Integer, Integer> valueCount = getValueCount(hand);
        for (Map.Entry<Integer, Integer> entry : valueCount.entrySet()) {
            if (entry.getValue() == 4) {
                return "Four of a Kind";
            }
        }
        return null;
    }

    private static String isFullHouse(Deck hand) {
        Map<Integer, Integer> valueCount = getValueCount(hand);
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
        int[] values = getSortedValues(hand);

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
        Map<Integer, Integer> valueCount = getValueCount(hand);
        for (Map.Entry<Integer, Integer> entry : valueCount.entrySet()) {
            if (entry.getValue() == 3) {
                return "Three of a Kind";
            }
        }
        return null;
    }

    private static String isTwoPair(Deck hand) {
        Map<Integer, Integer> valueCount = getValueCount(hand);
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
        Map<Integer, Integer> valueCount = getValueCount(hand);
        for (Map.Entry<Integer, Integer> entry : valueCount.entrySet()) {
            if (entry.getValue() == 2) {
                return "One Pair";
            }
        }
        return null;
    }

    public static int compareHands(PokerHandChecker hand1, PokerHandChecker hand2) {
        String handType1 = hand1.determineHand();
        String handType2 = hand2.determineHand();

        int rank1 = getHandRank(handType1);
        int rank2 = getHandRank(handType2);

        if (rank1 != rank2) {
            return rank1-rank2;
        } else {
            return compareSameTypeHands(hand1.hand, hand2.hand, handType1);
        }
    }

    private static int getHandRank(String handType) {
        String[] handRankings = {
            "High Card", "One Pair", "Two Pair", "Three of a Kind", "Straight", "Flush", 
            "Full House", "Four of a Kind", "Straight Flush", "Royal Flush"
        };
        for (int i = 0; i < handRankings.length; i++) {
            if (handRankings[i].equals(handType)) {
                return i;
            }
        }
        return -1; // Invalid hand type
    }

    private static int compareSameTypeHands(Deck hand1, Deck hand2, String handType) {
        int[] values1 = getSortedValues(hand1);
        int[] values2 = getSortedValues(hand2);

        switch (handType) {
            case "One Pair":
                return comparePairs(hand1, hand2, 2);
            case "Two Pair":
                return compareTwoPairs(hand1, hand2);
            case "Three of a Kind":
                return compareMultiples(hand1, hand2, 3);
            case "Four of a Kind":
                return compareMultiples(hand1, hand2, 4);
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

    private static int comparePairs(Deck hand1, Deck hand2, int pairSize) {
        Map<Integer, Integer> valueCount1 = getValueCount(hand1);
        Map<Integer, Integer> valueCount2 = getValueCount(hand2);

        int pairValue1 = getKeyForCount(valueCount1, pairSize);
        int pairValue2 = getKeyForCount(valueCount2, pairSize);

        if (pairValue1 != pairValue2) {
            return pairValue1 - pairValue2;
        } else {
            // Compare remaining cards if pairs have the same value
            return compareRemainingCards(hand1, hand2, pairValue1, pairSize);
        }
    }

    private static int compareTwoPairs(Deck hand1, Deck hand2) {
        int[] sortedValues1 = getSortedValues(hand1);
        int[] sortedValues2 = getSortedValues(hand2);

        // Get the higher pair values
        int highPairValue1 = getHighPairValue(hand1);
        int highPairValue2 = getHighPairValue(hand2);

        if (highPairValue1 != highPairValue2) {
            return highPairValue1 - highPairValue2;
        } else {
            // Compare remaining cards if higher pairs have the same value
            return compareRemainingCards(hand1, hand2, highPairValue1, 2);
        }
    }

    private static int getHighPairValue(Deck hand) {
        Map<Integer, Integer> valueCount = getValueCount(hand);

        List<Integer> pairValues = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : valueCount.entrySet()) {
            if (entry.getValue() == 2) {
                pairValues.add(entry.getKey());
            }
        }

        return Collections.max(pairValues);
    }

    private static int compareMultiples(Deck hand1, Deck hand2, int multipleSize) {
        Map<Integer, Integer> valueCount1 = getValueCount(hand1);
        Map<Integer, Integer> valueCount2 = getValueCount(hand2);

        int value1 = getKeyForCount(valueCount1, multipleSize);
        int value2 = getKeyForCount(valueCount2, multipleSize);

        return value1 - value2;
    }

    private static int compareFullHouse(Deck hand1, Deck hand2) {
        Map<Integer, Integer> valueCount1 = getValueCount(hand1);
        Map<Integer, Integer> valueCount2 = getValueCount(hand2);

        int three1 = getKeyForCount(valueCount1, 3);
        int three2 = getKeyForCount(valueCount2, 3);

        if (three1 != three2) return three1 - three2;
        return getKeyForCount(valueCount1, 2) - getKeyForCount(valueCount2, 2);
    }

    private static int compareRemainingCards(Deck hand1, Deck hand2, int excludeValue, int excludeCount) {
        int[] values1 = getSortedValuesExcluding(hand1, excludeValue, excludeCount);
        int[] values2 = getSortedValuesExcluding(hand2, excludeValue, excludeCount);
        return compareHighCard(values1, values2);
    }

    private static int[] getSortedValues(Deck hand) {
        int[] values = new int[hand.size()];
        for (int i = 0; i < hand.size(); i++) {
            int value = hand.getCard(i).getValue();
            values[i] = value == 1 ? 14 : value; // Ace can be high
        }
        Arrays.sort(values);
        return values;
    }

    private static int[] getSortedValuesExcluding(Deck hand, int excludeValue, int excludeCount) {
        int[] values = new int[hand.size() - excludeCount];
        int index = 0;
        for (int i = 0; i < hand.size(); i++) {
            int value = hand.getCard(i).getValue();
            if (value != excludeValue) {
                values[index++] = value;
            }
        }
        Arrays.sort(values);
        return values;
    }

    private static int compareHighCard(int[] values1, int[] values2) {
        for (int i = values1.length - 1; i >= 0; i--) {
            if (values1[i] != values2[i]) {
                return values1[i] - values2[i];
            }
        }
        return 0;
    }

    private static Map<Integer, Integer> getValueCount(Deck hand) {
        Map<Integer, Integer> valueCount = new HashMap<>();
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.getCard(i);
            valueCount.put(card.getValue(), valueCount.getOrDefault(card.getValue(), 0) + 1);
        }
        return valueCount;
    }

    private static int getKeyForCount(Map<Integer, Integer> valueCount, int count) {
        for (Map.Entry<Integer, Integer> entry : valueCount.entrySet()) {
            if (entry.getValue() == count) {
                return entry.getKey();
            }
        }
        return -1; // Not found
    }
}
