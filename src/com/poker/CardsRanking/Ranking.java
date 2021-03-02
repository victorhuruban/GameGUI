package com.poker.CardsRanking;

import com.poker.Pack.Card;

import java.util.*;

public class Ranking {
    private ArrayList<Card> playerCards;
    private ArrayList<Card> sharedCards;
    private int score;
    private String hand;

    public Ranking() {
        this.playerCards = new ArrayList<>();
        this.sharedCards = new ArrayList<>();
        score = 0;
        hand = "";
    }

    public void addPlayerCard(ArrayList<Card> playerCards) {
        this.playerCards = playerCards;
    }

    public void addSharedCard(ArrayList<Card> sharedCards) {
        this.sharedCards = sharedCards;
    }

    public int getScoreValue() {
        return score;
    }

    public int getScore() {
        checkHighestCard();
        checkPairs();

        return score;
    }

    public void checkHighestCard() {
        int temp;
        for (Card card: playerCards) {
            switch (card.getValue()) {
                case "two":
                    temp = 1;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + "+" + card.getType();
                    }
                    continue;
                case "three":
                    temp = 2;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + "+" + card.getType();
                    }
                    continue;
                case "four":
                    temp = 3;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + "+" + card.getType();
                    }
                    continue;
                case "five":
                    temp = 4;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + "+" + card.getType();
                    }
                    continue;
                case "six":
                    temp = 5;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + "+" + card.getType();
                    }
                    continue;
                case "seven":
                    temp = 6;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + "+" + card.getType();
                    }
                    continue;
                case "eight":
                    temp = 7;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + "+" + card.getType();
                    }
                    continue;
                case "nine":
                    temp = 8;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + "+" + card.getType();
                    }
                    continue;
                case "ten":
                    temp = 9;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + "+" + card.getType();
                    }
                    continue;
                case "jack":
                    temp = 10;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + "+" + card.getType();
                    }
                    continue;
                case "queen":
                    temp = 11;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + "+" + card.getType();
                    }
                    continue;
                case "king":
                    temp = 12;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + "+" + card.getType();
                    }
                    continue;
                case "ace":
                    temp = 13;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + "+" + card.getType();
                    }
            }
        }
    }

    private void checkCards(ArrayList<Card> cards) {
        boolean dp = false;
        boolean tp = false;
        ArrayList<Integer> holder = new ArrayList<>();
        HashMap<String, Integer> dict = new HashMap<>();

        for (Card card : cards) {
            if (dict.containsKey(card.getValue())) {
                dict.put(card.getValue(), dict.get(card.getValue()) + 1);
            } else dict.put(card.getValue(), 1);
        }

        int tempScore = 0;
        for (Map.Entry<String, Integer> entry: dict.entrySet()) {
            int tempVal = 0;
            if (entry.getValue() == 4) {
                tempVal = fourPair(entry.getKey());
                if (tempVal > tempScore) { // FOUR PAIR CHECK
                    tempScore = tempVal;
                }
                continue;
            }
            if (entry.getValue() == 3 && !tp && !dp) {
                tempVal = triplePair(entry.getKey());
                if (tempVal > tempScore) {
                    tempScore = tempVal;
                }
                holder.add(tempVal);
                tp = true;
                continue;
            }
            if (entry.getValue() == 3 && !tp) {
                tempVal = triplePair(entry.getKey());
                tempVal += 49;
                if (tempVal > tempScore) {
                    tempScore = tempVal; // FULL HOUSE CHECK
                }
                continue;
            }
            if (entry.getValue() == 2 && !dp && !tp) {
                tempVal = doublePair(entry.getKey());
                if (tempVal > tempScore) {
                    tempScore = tempVal;
                }
                holder.add(tempVal);
                dp = true;
                continue;
            }
            if (entry.getValue() == 2 && !dp) {
                int tempH = holder.get(0) + 49;
                if (tempH > tempScore) { // FULL HOUSE CHECK
                    tempScore = tempH;
                }
                dp = true;
                continue;
            }
            if (entry.getValue() == 2 && !tp) {
                tempVal = doublePair(entry.getKey());
                if (tempVal > tempScore && tempVal > holder.get(0)) { // DOUBLE PAIR CHECK
                    tempScore = tempVal;
                }
            }
        }
        if (tempScore > score) {
            score = tempScore;
        }
    }

    public boolean checkStraight(ArrayList<Card> cards) {
        int[] tempArr = new int[5];
        for (int i = 0; i < cards.size(); i++) {
            tempArr[i] = getCardValue(cards.get(i));
        }
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < tempArr.length - 1; i++) {
                if (tempArr[i] > tempArr[i + 1]) {
                    int temp = tempArr[i];
                    tempArr[i] = tempArr[i + 1];
                    tempArr[i + 1] = temp;
                    sorted = false;
                }
            }
        }
        for (int i = 0; i < tempArr.length - 1; i++) {
            if (tempArr[i] + 1 != tempArr[i + 1]) {
                return false;
            }
        }
        return true;
    }

    public int getStraightValue(ArrayList<Card> cards) {
        int max = Integer.MIN_VALUE;
        for (Card card : cards) {
            int temp = getStraightValue(getCardValue(card));
            if (temp > max) {
                max = temp;
            }
        }
        return max;
    }

    public boolean checkFlush(ArrayList<Card> cards) {
        HashSet<String> tempSet = new HashSet<>();
        for (Card card : cards) {
            tempSet.add(card.getType());
        }
        return tempSet.size() == 1;
    }

    public void checkPairs() {
        if (sharedCards.size() == 3) {
            ArrayList<Card> pairOfFive = new ArrayList<>(playerCards);
            pairOfFive.addAll(sharedCards);
            checkCards(pairOfFive);
            checkTwoAndThree(getSharedThreePairs());
        } else if (sharedCards.size() == 4){
            checkTwoAndThree(getSharedThreePairs());
            checkOneAndFour(getSharedFourPairs());
        } else if (sharedCards.size() == 5){
            checkTwoAndThree(getSharedThreePairs());
            checkOneAndFour(getSharedFourPairs());
        }
    }

    private void checkTwoAndThree(ArrayList<ArrayList<Card>> cards) {
        for (ArrayList<Card> card : cards) {
            int tempScore = 0;
            ArrayList<Card> tempArr = new ArrayList<>(playerCards);
            tempArr.addAll(card);
            checkCards(tempArr);
            if (checkStraight(tempArr) && checkFlush(tempArr) && getStraightValue(tempArr) == 73) {
                tempScore = 1000;
            }
            if (checkStraight(tempArr) && checkFlush(tempArr)) {
                int temp = getStraightValue(tempArr);
                if (temp + 62 > tempScore) {
                    tempScore = temp + 62;
                }
            }
            if (checkStraight(tempArr)) {
                int temp = getStraightValue(tempArr);
                if (getStraightValue(temp) > tempScore) {
                    tempScore = getStraightValue(temp);
                }
            }
            if (checkFlush(tempArr)) {
                if (tempScore < 100) {
                    tempScore = 100;
                }
            }
            if (tempScore > score) {
                score = tempScore;
            }
        }
     }

    private void checkOneAndFour(ArrayList<ArrayList<Card>> cards) {
        for (ArrayList<Card> card : cards) {
            int tempScore = 0;
            ArrayList<Card> tempArr = new ArrayList<>();
            tempArr.add(playerCards.get(0));
            tempArr.addAll(card);
            checkCards(tempArr);
            if (checkStraight(tempArr) && checkFlush(tempArr) && getStraightValue(tempArr) == 73) {
                tempScore = 1000;
            }
            if (checkStraight(tempArr) && checkFlush(tempArr)) {
                int temp = getStraightValue(tempArr);
                if (temp + 62 > tempScore) {
                    tempScore = temp + 62;
                }
            }
            if (checkStraight(tempArr)) {
                int temp = getStraightValue(tempArr);
                if (getStraightValue(temp) > tempScore) {
                    tempScore = getStraightValue(temp);
                }
            }
            if (checkFlush(tempArr)) {
                if (tempScore < 100) {
                    tempScore = 100;
                }
            }
            if (score < tempScore) {
                score = tempScore;
            }
        }
        for (ArrayList<Card> card : cards) {
            int tempScore = 0;
            ArrayList<Card> tempArr = new ArrayList<>();
            tempArr.add(playerCards.get(1));
            tempArr.addAll(card);
            checkCards(tempArr);
            if (checkStraight(tempArr) && checkFlush(tempArr) && getStraightValue(tempArr) == 73) {
                tempScore = 1000;
            }
            if (checkStraight(tempArr) && checkFlush(tempArr)) {
                int temp = getStraightValue(tempArr);
                if (temp + 62 > tempScore) {
                    tempScore = temp + 62;
                }
            }
            if (checkStraight(tempArr)) {
                int temp = getStraightValue(tempArr);
                if (getStraightValue(temp) > tempScore) {
                    tempScore = getStraightValue(temp);
                }
            }
            if (checkFlush(tempArr)) {
                if (tempScore < 100) {
                    tempScore = 100;
                }
            }
            if (tempScore > score) {
                score = tempScore;
            }
        }
    }

    private ArrayList<ArrayList<Card>> getSharedThreePairs() {
        ArrayList<ArrayList<Card>> cds = new ArrayList<>();
        for (int i = 0; i < sharedCards.size() - 2; i++) {
            for (int j = i + 1; j < sharedCards.size() - 1; j++) {
                for (int z = j + 1; z < sharedCards.size(); z++) {
                    ArrayList<Card> tst = new ArrayList<>();
                    tst.add(sharedCards.get(i));
                    tst.add(sharedCards.get(j));
                    tst.add(sharedCards.get(z));
                    cds.add(tst);
                }
            }
        }
        return cds;
    }

    private ArrayList<ArrayList<Card>> getSharedFourPairs() {
        ArrayList<ArrayList<Card>> cds = new ArrayList<>();
        if (sharedCards.size() == 4) {
            cds.add(sharedCards);
        } else {
            for (int i = 0; i < sharedCards.size(); i++) {
                ArrayList<Card> tst = new ArrayList<>();
                for (int j = 0; j < sharedCards.size(); j++) {
                    if (j != i) {
                        tst.add(sharedCards.get(j));
                    }
                }
                cds.add(tst);
            }
        }
        return cds;
    }


    private int doublePair(String value) {
        int temp = 0;
        switch (value) {
            case "two":
                temp = 14;
                break;
            case "three":
                temp = 15;
                break;
            case "four":
                temp = 16;
                break;
            case "five":
                temp = 17;
                break;
            case "six":
                temp = 18;
                break;
            case "seven":
                temp = 19;
                break;
            case "eight":
                temp = 20;
                break;
            case "nine":
                temp = 21;
                break;
            case "ten":
                temp = 22;
                break;
            case "jack":
                temp = 23;
                break;
            case "queen":
                temp = 24;
                break;
            case "king":
                temp = 25;
                break;
            case "ace":
                temp = 26;
                break;
        }
        return temp;
    }

    private int triplePair(String value) {
        int temp = 0;
        switch (value) {
            case "two":
                temp = 52;
                break;
            case "three":
                temp = 53;
                break;
            case "four":
                temp = 54;
                break;
            case "five":
                temp = 55;
                break;
            case "six":
                temp = 56;
                break;
            case "seven":
                temp = 57;
                break;
            case "eight":
                temp = 58;
                break;
            case "nine":
                temp = 59;
                break;
            case "ten":
                temp = 60;
                break;
            case "jack":
                temp = 61;
                break;
            case "queen":
                temp = 62;
                break;
            case "king":
                temp = 63;
                break;
            case "ace":
                temp = 64;
                break;
        }
        return temp;
    }

    private int fourPair(String value) {
        int temp = 0;
        switch (value) {
            case "two":
                temp = 114;
                break;
            case "three":
                temp = 115;
                break;
            case "four":
                temp = 116;
                break;
            case "five":
                temp = 117;
                break;
            case "six":
                temp = 118;
                break;
            case "seven":
                temp = 119;
                break;
            case "eight":
                temp = 120;
                break;
            case "nine":
                temp = 121;
                break;
            case "ten":
                temp = 122;
                break;
            case "jack":
                temp = 123;
                break;
            case "queen":
                temp = 124;
                break;
            case "king":
                temp = 125;
                break;
            case "ace":
                temp = 126;
                break;
        }
        return temp;
    }

    public int getCardValue(Card card) {
        switch (card.getValue()) {
            case "two":
                return 1;
            case "three":
                return 2;
            case "four":
                return 3;
            case "five":
                return 4;
            case "six":
                return 5;
            case "seven":
                return 6;
            case "eight":
                return 7;
            case "nine":
                return 8;
            case "ten":
                return 9;
            case "jack":
                return 10;
            case "queen":
                return 11;
            case "king":
                return 12;
            case "ace":
                return 13;
        }
        return -1;
    }

    private int getStraightValue(int value) {
        switch (value) {
            case 5:
                return 65;
            case 6:
                return 66;
            case 7:
                return 67;
            case 8:
                return 68;
            case 9:
                return 69;
            case 10:
                return 70;
            case 11:
                return 71;
            case 12:
                return 72;
            case 13:
                return 73;
        }
        return -1;
    }
}
