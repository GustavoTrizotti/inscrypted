package br.edu.ifsp.scl.core;

public class Card {
    private final String tag;
    private final Cost sacrificeCost;
    private final int damage;
    private int sacrifices;
    private int life;

    Card(String tag, Cost sacrificeCost, int damage, int life) {
        this.tag = tag;
        this.sacrificeCost = sacrificeCost;
        this.damage = damage;
        this.life = life;
    }

    static Card identity(Cost sacrificeCost) {
        return new Card("Dummy", sacrificeCost, 1, 1);
    }

    static Card createSquirrel() {
        return new Card("Squirrel", Cost.ZERO, 0, 0);
    }

    boolean isCostReached() {
        return sacrificeCost.getCost() - sacrifices == 0;
    }

    void addSacrifice() {
        sacrifices++;
    }

    String getTag() {
        return tag;
    }

    Cost getCost() {
        return sacrificeCost;
    }

    int getDamage() {
        return damage;
    }

    int getLife() {
        return life;
    }

    void reduceLifeBy(int attack) {
        life -= attack;
    }

    @Override
    public String toString() {
        return """
                ┌──────────────────┐
                │ %-16s │
                │ Cost: %-10d │
                │ Dmg:  %-10d │
                │ HP:   %-10d │
                └──────────────────┘
                """.formatted(tag, sacrificeCost.getCost(), damage, life);
    }
}
