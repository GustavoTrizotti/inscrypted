package br.edu.ifsp.scl.inscrypted;

public enum Cost {
    ZERO(0),
    ONE(1);

    private final int cost;

    Cost(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }
}
