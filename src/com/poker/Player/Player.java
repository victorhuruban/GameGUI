package com.poker.Player;

import com.poker.CardsRanking.Ranking;
import com.poker.Pack.Card;
import com.poker.Pack.Pack;

import java.util.ArrayList;

public class Player {
    private ArrayList<Card> ownCards;
    private ArrayList<Card> sharedCards;
    private Ranking rank;
    private String name;
    private boolean folded;
    private int money;
    private Pack pack;

    public Player(String name, int money, Pack pack) {
        this.name = name;
        this.money = money;
        this.pack = pack;
        rank = new Ranking();
        ownCards = new ArrayList<>();
        sharedCards = new ArrayList<>();
        folded = false;

        this.addCards(new Card("two", "diamond"));
        this.addCards(pack.popCard());

        this.addSharedCards(pack.popCard());
        this.addSharedCards(new Card("two", "spade"));
        this.addSharedCards(pack.popCard());
        this.addSharedCards(new Card("ten", "diamond"));
        this.addSharedCards(new Card("ten", "club"));

        rank.addPlayerCard(ownCards);
        rank.addSharedCard(sharedCards);
    }

    public void fold() {
        folded = true;
    }

    public int raise(int sum) {
        if (sum > money) {
            System.out.println("Not enough money, Try again");
            return -1;
        } else {
            money -= sum;
            return sum;
        }
    }

    public int allIn() {
        int all = money;
        money = 0;

        return all;
    }

    public Ranking getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Card> getCards() {
        return ownCards;
    }

    public void addCards(Card card) {
        if (ownCards.size() != 2) {
            ownCards.add(card);
        } else {
            System.out.println("Full card ArrayList");
        }
    }

    public void addSharedCards(Card card) {
        if (sharedCards.size() < 5) {
            sharedCards.add(card);
        } else {
            System.out.println("All cards turned");
        }
    }

    public void displayCards() {
        System.out.print("OWN: " + ownCards.get(0).getValue() + ":" + ownCards.get(0).getType() + " , " +
                ownCards.get(1).getValue() + ":" + ownCards.get(1).getType() + "\n");
        System.out.print("SHARED: " + sharedCards.get(0).getValue() + ":" + sharedCards.get(0).getType() + " , " +
                sharedCards.get(1).getValue() + ":" + sharedCards.get(1).getType() + " , " +
                sharedCards.get(2).getValue() + ":" + sharedCards.get(2).getType() + " , " +
                sharedCards.get(3).getValue() + ":" + sharedCards.get(3).getType() + " , " +
                sharedCards.get(4).getValue() + ":" + sharedCards.get(4).getType() + "\n");
    }
}
