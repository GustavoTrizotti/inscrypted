package br.edu.ifsp.scl.core;

import java.util.HashSet;
import java.util.Set;

final class Round {
    private final Table table;
    private TableRow attackingRow;
    private TableRow opposingRow;

    Round(Table table) {
        this.table = table;
    }

    RoundResult calculateResultFromPlayerRow() {
        configureRowsForPlayerRound();
        return calculateRoundResult();
    }

    RoundResult calculateResultFromOpponentRow() {
        configureRowsForOpponentRound();
        return calculateRoundResult();
    }

    private RoundResult calculateRoundResult() {
        final Set<TableSlot> toEliminate = new HashSet<>();
        int directDamage = 0;

        for (TableSlot slot : TableSlot.values()) {
            var attacking = attackingRow.getCardAtSlot(slot);
            var opposed = opposingRow.getCardAtSlot(slot);

            if (attacking.isEmpty()) continue;
            Card attacker = attacking.get();

            if (opposed.isEmpty()) {
                directDamage += attacker.getDamage();
                continue;
            }

            var affected = opposed.get();
            affected.reduceLifeBy(attacker.getDamage());

            if (!affected.isDead()) continue;

            toEliminate.add(slot);
        }

        return new RoundResult(directDamage, toEliminate);
    }

    private void configureRowsForPlayerRound() {
        this.attackingRow = table.playerRow();
        this.opposingRow = table.opponentRow();
    }

    private void configureRowsForOpponentRound() {
        this.attackingRow = table.opponentRow();
        this.opposingRow = table.playerRow();
    }
}
