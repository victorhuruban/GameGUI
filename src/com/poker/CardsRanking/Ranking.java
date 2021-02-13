package com.poker.CardsRanking;

import com.poker.Pack.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ranking {
    private ArrayList<Card> playerCards;
    private ArrayList<Card> sharedCards;
    private int score;
    private String hand;

    public Ranking() {
        playerCards = new ArrayList<>();
        sharedCards = new ArrayList<>();
        score = 0;
        hand = "highest card";
    }

    public void addPlayerCard(ArrayList<Card> playerCards) {
        this.playerCards = playerCards;
    }

    public void addSharedCard(ArrayList<Card> sharedCards) {
        this.sharedCards = sharedCards;
    }

    public void getScore() {
        checkHighestCard();
        checkPairs();
        System.out.println(score);
    }

    public void checkHighestCard() {
        int temp;
        for (Card card: playerCards) {
            switch (card.getValue()) {
                case "two":
                    temp = 1;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + " of " + card.getType();
                    }
                    continue;
                case "three":
                    temp = 2;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + " of " + card.getType();
                    }
                    continue;
                case "four":
                    temp = 3;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + " of " + card.getType();
                    }
                    continue;
                case "five":
                    temp = 4;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + " of " + card.getType();
                    }
                    continue;
                case "six":
                    temp = 5;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + " of " + card.getType();
                    }
                    continue;
                case "seven":
                    temp = 6;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + " of " + card.getType();
                    }
                    continue;
                case "eight":
                    temp = 7;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + " of " + card.getType();
                    }
                    continue;
                case "nine":
                    temp = 8;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + " of " + card.getType();
                    }
                    continue;
                case "ten":
                    temp = 9;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + " of " + card.getType();
                    }
                    continue;
                case "jack":
                    temp = 10;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + " of " + card.getType();
                    }
                    continue;
                case "queen":
                    temp = 11;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + " of " + card.getType();
                    }
                    continue;
                case "king":
                    temp = 12;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + " of " + card.getType();
                    }
                    continue;
                case "ace":
                    temp = 13;
                    if (score < temp) {
                        score = temp;
                        hand = card.getValue() + " of " + card.getType();
                    }
            }
        }
    }

    public void checkPairs() {
        boolean dp = false;
        HashMap<String, Integer> dict = new HashMap<>();
        ArrayList<Card> tempArr = new ArrayList<>(playerCards);
        tempArr.addAll(sharedCards);

        for (Card card : tempArr) {
            System.out.print(card.getValue() + " " + card.getType() + " : ");
            if (dict.containsKey(card.getValue())) {
                dict.put(card.getValue(), dict.get(card.getValue()) + 1);
            } else dict.put(card.getValue(), 1);
        }

        for (Map.Entry<String, Integer> entry: dict.entrySet()) {
            if (entry.getValue() == 2 && dp) {
                score += doublePair(entry.getKey());
            }
            if (entry.getValue() == 2 && !dp) {
                score = doublePair(entry.getKey());
                dp = true;
            }
        }
    }

    private int doublePair(String value) {
        int temp = 0;
        switch (value) {
            case "two":
                temp = 14;
                if (score < temp) {
                    score = temp;
                    hand = "Pair: " + value;
                }
                break;
            case "three":
                temp = 15;
                if (score < temp) {
                    score = temp;
                    hand = "Pair: " + value;
                }
                break;
            case "four":
                temp = 16;
                if (score < temp) {
                    score = temp;
                    hand = "Pair: " + value;
                }
                break;
            case "five":
                temp = 17;
                if (score < temp) {
                    score = temp;
                    hand = "Pair: " + value;
                }
                break;
            case "six":
                temp = 18;
                if (score < temp) {
                    score = temp;
                    hand = "Pair: " + value;
                }
                break;
            case "seven":
                temp = 19;
                if (score < temp) {
                    score = temp;
                    hand = "Pair: " + value;
                }
                break;
            case "eight":
                temp = 20;
                if (score < temp) {
                    score = temp;
                    hand = "Pair: " + value;
                }
                break;
            case "nine":
                temp = 21;
                if (score < temp) {
                    score = temp;
                    hand = "Pair: " + value;
                }
                break;
            case "ten":
                temp = 22;
                if (score < temp) {
                    score = temp;
                    hand = "Pair: " + value;
                }
                break;
            case "jack":
                temp = 23;
                if (score < temp) {
                    score = temp;
                    hand = "Pair: " + value;
                }
                break;
            case "queen":
                temp = 24;
                if (score < temp) {
                    score = temp;
                    hand = "Pair: " + value;
                }
                break;
            case "king":
                temp = 25;
                if (score < temp) {
                    score = temp;
                    hand = "Pair: " + value;
                }
                break;
            case "ace":
                temp = 26;
                if (score < temp) {
                    score = temp;
                    hand = "Pair: " + value;
                }
                break;
        }
        return temp;
    }
}
