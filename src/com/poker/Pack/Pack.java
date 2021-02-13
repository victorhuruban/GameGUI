package com.poker.Pack;

import java.util.Collections;
import java.util.Stack;

public class Pack {
    private Stack<Card> cardPack;

    public Pack() {
        cardPack = new Stack<>();
        initializePack();
        shufflePack();
    }

    private void initializePack() {
        cardPack.push(new Card("two", "club"));
        cardPack.push(new Card("three", "club"));
        cardPack.push(new Card("four", "club"));
        cardPack.push(new Card("five", "club"));
        cardPack.push(new Card("six", "club"));
        cardPack.push(new Card("seven", "club"));
        cardPack.push(new Card("eight", "club"));
        cardPack.push(new Card("nine", "club"));
        cardPack.push(new Card("ten", "club"));
        cardPack.push(new Card("jack", "club"));
        cardPack.push(new Card("queen", "club"));
        cardPack.push(new Card("king", "club"));
        cardPack.push(new Card("ace", "club"));

        cardPack.push(new Card("two", "diamond"));
        cardPack.push(new Card("three", "diamond"));
        cardPack.push(new Card("four", "diamond"));
        cardPack.push(new Card("five", "diamond"));
        cardPack.push(new Card("six", "diamond"));
        cardPack.push(new Card("seven", "diamond"));
        cardPack.push(new Card("eight", "diamond"));
        cardPack.push(new Card("nine", "diamond"));
        cardPack.push(new Card("ten", "diamond"));
        cardPack.push(new Card("jack", "diamond"));
        cardPack.push(new Card("queen", "diamond"));
        cardPack.push(new Card("king", "diamond"));
        cardPack.push(new Card("ace", "diamond"));

        cardPack.push(new Card("two", "spade"));
        cardPack.push(new Card("three", "spade"));
        cardPack.push(new Card("four", "spade"));
        cardPack.push(new Card("five", "spade"));
        cardPack.push(new Card("six", "spade"));
        cardPack.push(new Card("seven", "spade"));
        cardPack.push(new Card("eight", "spade"));
        cardPack.push(new Card("nine", "spade"));
        cardPack.push(new Card("ten", "spade"));
        cardPack.push(new Card("jack", "spade"));
        cardPack.push(new Card("queen", "spade"));
        cardPack.push(new Card("king", "spade"));
        cardPack.push(new Card("ace", "spade"));

        cardPack.push(new Card("two", "heart"));
        cardPack.push(new Card("three", "heart"));
        cardPack.push(new Card("four", "heart"));
        cardPack.push(new Card("five", "heart"));
        cardPack.push(new Card("six", "heart"));
        cardPack.push(new Card("seven", "heart"));
        cardPack.push(new Card("eight", "heart"));
        cardPack.push(new Card("nine", "heart"));
        cardPack.push(new Card("ten", "heart"));
        cardPack.push(new Card("jack", "heart"));
        cardPack.push(new Card("queen", "heart"));
        cardPack.push(new Card("king", "heart"));
        cardPack.push(new Card("ace", "heart"));
    }

    private void shufflePack() {
        Collections.shuffle(cardPack);
    }

    public Card popCard() {
        return cardPack.pop();
    }

    /*public void listPack() {
        while (!cardPack.isEmpty()) {
            Card temp = cardPack.pop();
            System.out.println(temp.getValue() + " " + temp.getType());
        }
    }*/
}
