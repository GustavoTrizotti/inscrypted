package br.edu.ifsp.scl.inscrypted;

public class Card {
    private final String tag;
    private final Cost sacrificeCost;
    private int damage;
    private int sacrifices;
    private int life;

    public Card(String tag) {
        this(tag, 1, 1);
    }

    public Card(String tag, int lifePoints, int damage) {
        this(tag, Cost.ZERO);
        this.damage = damage;
        this.life = lifePoints;
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

    public int getDamage() {
        return damage;
    }

    public int getLife() {
        return life;
    }

    public void reduceLifeBy(int attack) {
        life -= attack;
    }
}
