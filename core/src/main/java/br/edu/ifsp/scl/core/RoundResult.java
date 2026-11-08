package br.edu.ifsp.scl.core;

import java.util.Set;

public record RoundResult(int directDamage, Set<TableSlot> slotsToEliminate) {
    public RoundResult(int directDamage, Set<TableSlot> slotsToEliminate) {
        this.directDamage = directDamage;
        this.slotsToEliminate = Set.copyOf(slotsToEliminate);
    }
}
