package br.edu.ifsp.scl.inscrypted;

public class Card {
    private final String tag;
    private final Cost sacrificeCost;
    private int sacrifices;

    public Card(String tag) {
        this.tag = tag;
        this.sacrificeCost = Cost.ZERO;
    }

    public Card(String tag, Cost sacrificeCost) {
        this.tag = tag;
        this.sacrificeCost = sacrificeCost;
    }

    public boolean isCostReached() {
        return sacrificeCost.getCost() - sacrifices == 0;
    }

    public void addSacrifice() {
        sacrifices++;
    }

    public String getTag() {
        return tag;
    }

    public Cost getCost() {
        return sacrificeCost;
    }

    public int getAttack() {
        return 1;
    }
}
