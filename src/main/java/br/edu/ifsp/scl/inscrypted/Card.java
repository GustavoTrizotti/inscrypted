package br.edu.ifsp.scl.inscrypted;

public class Card {
    private final String tag;
    private final Cost sacrificeCost;
    private final int damage;
    private int sacrifices;
    private int life;

    public Card(String tag, Cost sacrificeCost, int damage, int life) {
        this.tag = tag;
        this.sacrificeCost = sacrificeCost;
        this.damage = damage;
        this.life = life;
    }

    public static Card identity(Cost sacrificeCost) {
        return new Card("Dummy", sacrificeCost, 1, 1);
    }

    public static Card createSquirrel() {
        return new Card("Squirrel", Cost.ZERO, 0, 0);
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
