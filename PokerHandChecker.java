
public class PokerHandChecker {

    private Deck hand;
    private MyHashMap<Integer, Integer> valueCount;

    public PokerHandChecker(Deck hand) {
        this.hand = hand;
        this.valueCount = new MyHashMap<>();
        calculateValueCount();
    }

    public String determineHand() {
        String result;
        if ((result = isRoyalFlush(hand)) != null)
            return result;
        if ((result = isStraightFlush(hand)) != null)
            return result;
        if ((result = isFourOfAKind(hand)) != null)
            return result;
        if ((result = isFullHouse(hand)) != null)
            return result;
        if ((result = isFlush(hand)) != null)
            return result;
        if ((result = isStraight(hand)) != null)
            return result;
        if ((result = isThreeOfAKind(hand)) != null)
            return result;
        if ((result = isTwoPair(hand)) != null)
            return result;
        if ((result = isOnePair(hand)) != null)
            return result;
        return "High Card";
    }

    private void calculateValueCount() {
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.getCard(i);
            int value = card.getValue();
            int count = valueCount.getOrDefault(value, 0);
            valueCount.put(value, count + 1);
        }
    }

    private String isRoyalFlush(Deck hand) {
        if (isStraightFlush(hand) != null) {
            DLList<Integer> keys = valueCount.keySet().toDLList();
            for (int i = 0; i < keys.size(); i++) {
                if (keys.get(i) == 1) {
                    return "Royal Flush";
                }
            }
        }
        return null;
    }

    private String isStraightFlush(Deck hand) {
        if (isFlush(hand) != null && isStraight(hand) != null) {
            return "Straight Flush";
        }
        return null;
    }

    private String isFourOfAKind(Deck hand) {
        DLList<Integer> keys = valueCount.keySet().toDLList();
        for (int i = 0; i < keys.size(); i++) {
            int count = valueCount.get(keys.get(i));
            if (count == 4) {
                return "Four of a Kind";
            }
        }
        return null;
    }

    private String isFullHouse(Deck hand) {
        boolean hasThree = false;
        boolean hasTwo = false;
        DLList<Integer> keys = valueCount.keySet().toDLList();
        for (int i = 0; i < keys.size(); i++) {
            int count = valueCount.get(keys.get(i));
            if (count == 3) {
                hasThree = true;
            }
            if (count == 2) {
                hasTwo = true;
            }
        }
        if (hasThree && hasTwo) {
            return "Full House";
        }
        return null;
    }

    private String isFlush(Deck hand) {
        String suit = hand.getCard(0).getSuit();
        for (int i = 1; i < hand.size(); i++) {
            if (!hand.getCard(i).getSuit().equals(suit))
                return null;
        }
        return "Flush";
    }

    private String isStraight(Deck hand) {
        DLList<Integer> values = getSortedValues(hand);

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
        if (values.get(0) == 2 && values.get(1) == 3 && values.get(2) == 4 && values.get(3) == 5
                && values.get(4) == 14) {
            return "Straight";
        }
        return null;
    }

    private String isThreeOfAKind(Deck hand) {
        DLList<Integer> keys = valueCount.keySet().toDLList();
        for (int i = 0; i < keys.size(); i++) {
            int count = valueCount.get(keys.get(i));
            if (count == 3) {
                return "Three of a Kind";
            }
        }
        return null;
    }

    private String isTwoPair(Deck hand) {
        int pairCount = 0;
        DLList<Integer> keys = valueCount.keySet().toDLList();
        for (int i = 0; i < keys.size(); i++) {
            int count = valueCount.get(keys.get(i));
            if (count == 2) {
                pairCount++;
            }
        }
        if (pairCount == 2) {
            return "Two Pair";
        }
        return null;
    }

    private String isOnePair(Deck hand) {
        DLList<Integer> keys = valueCount.keySet().toDLList();
        for (int i = 0; i < keys.size(); i++) {
            int count = valueCount.get(keys.get(i));
            if (count == 2) {
                return "One Pair";
            }
        }
        return null;
    }

    public static int compareHands(PokerHandChecker hand1, PokerHandChecker hand2, String handType1, String handType2) {

        System.out.println(handType1 + ": hand type 1");
        System.out.println(handType2 + ": hand type 2");

        int rank1 = getHandRank(handType1);
        int rank2 = getHandRank(handType2);

        if (rank1 != rank2) {
            return rank1 - rank2;
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
        DLList<Integer> values1 = getSortedValues(hand1);
        DLList<Integer> values2 = getSortedValues(hand2);

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
        MyHashMap<Integer, Integer> valueCount1 = getValueCount(hand1);
        MyHashMap<Integer, Integer> valueCount2 = getValueCount(hand2);

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
        DLList<Integer> sortedValues1 = getSortedValues(hand1);
        DLList<Integer> sortedValues2 = getSortedValues(hand2);

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
        MyHashMap<Integer, Integer> valueCount = getValueCount(hand);
        DLList<Integer> pairValues = new DLList<>();
        for (int i = 0; i < valueCount.size(); i++) {
            if (valueCount.get(i)!=null && valueCount.get(i) == 2) {
                pairValues.add(i);
            }
        }
        return pairValues.get(pairValues.size() - 1);
    }

    private static int compareMultiples(Deck hand1, Deck hand2, int multipleSize) {
        MyHashMap<Integer, Integer> valueCount1 = getValueCount(hand1);
        MyHashMap<Integer, Integer> valueCount2 = getValueCount(hand2);

        int value1 = getKeyForCount(valueCount1, multipleSize);
        int value2 = getKeyForCount(valueCount2, multipleSize);

        return value1 - value2;
    }

    private static int compareFullHouse(Deck hand1, Deck hand2) {
        MyHashMap<Integer, Integer> valueCount1 = getValueCount(hand1);
        MyHashMap<Integer, Integer> valueCount2 = getValueCount(hand2);

        int three1 = getKeyForCount(valueCount1, 3);
        int three2 = getKeyForCount(valueCount2, 3);

        if (three1 != three2)
            return three1 - three2;
        return getKeyForCount(valueCount1, 2) - getKeyForCount(valueCount2, 2);
    }

    private static int compareRemainingCards(Deck hand1, Deck hand2, int excludeValue, int excludeCount) {
        DLList<Integer> values1 = getSortedValuesExcluding(hand1, excludeValue, excludeCount);
        DLList<Integer> values2 = getSortedValuesExcluding(hand2, excludeValue, excludeCount);
        return compareHighCard(values1, values2);
    }

    private static DLList<Integer> getSortedValues(Deck hand) {
        DLList<Integer> values = new DLList<>();
        for (int i = 0; i < hand.size(); i++) {
            int value = hand.getCard(i).getValue();
            values.add(value == 1 ? 14 : value); // Ace can be high
        }
        return values;
    }

    private static DLList<Integer> getSortedValuesExcluding(Deck hand, int excludeValue, int excludeCount) {
        DLList<Integer> values = new DLList<>();
        for (int i = 0; i < hand.size(); i++) {
            int value = hand.getCard(i).getValue();
            if (value != excludeValue) {
                values.add(value);
            }
        }
        values.sort(); // Assuming DLList has a sort method
        return values;
    }

    private static int compareHighCard(DLList<Integer> values1, DLList<Integer> values2) {
        for (int i = values1.size() - 1; i >= 0; i--) {
            if (!values1.get(i).equals(values2.get(i))) {
                return values1.get(i) - values2.get(i);
            }
        }
        return 0;
    }

    private static MyHashMap<Integer, Integer> getValueCount(Deck hand) {
        MyHashMap<Integer, Integer> valueCount = new MyHashMap<>();
        for (int i = 0; i < hand.size(); i++) {
            int value = hand.getCard(i).getValue();
            int count = valueCount.getOrDefault(value, 0);
            valueCount.put(value, count + 1);
        }
        return valueCount;
    }

    private static int getKeyForCount(MyHashMap<Integer, Integer> valueCount, int count) {
        for (int i = 0; i < valueCount.size(); i++) {
            if (valueCount.get(i) != null) {
                if (valueCount.get(i) == count) {
                    return i;
                }

            }

        }
        return -1; // Not found
    }

}
