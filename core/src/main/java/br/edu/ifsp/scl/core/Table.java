package br.edu.ifsp.scl.core;

import java.util.*;

public final class Table {
    private final Map<Row, TableRow> rows = new HashMap<>();

    Table() {
        rows.put(Row.PLAYER, new TableRow());
        rows.put(Row.OPPONENT, new TableRow());
    }

    Optional<Card> getPlayerCardAtSlot(TableSlot slot) {
        return playerRow().getCardAtSlot(slot);
    }

    Optional<Card> getOpponentCardAtSlot(TableSlot slot) {
        return opponentRow().getCardAtSlot(slot);
    }

    void insertCardAtSlot(TableSlot slot, Card card) {
        playerRow().insertCardAtSlot(slot, card);
    }

    void insertOpponentCardAtSlot(TableSlot slot, Card card) {
        opponentRow().insertCardAtSlot(slot, card);
    }

    void removeCardAtSlot(TableSlot slot) {
        playerRow().removeCardAtSlot(slot);
    }

    void removeOpponentCardAtSlot(TableSlot slot) {
        opponentRow().removeCardAtSlot(slot);
    }

    List<Card> getOpponentCards() {
        return List.copyOf(opponentRow().cards());
    }

    List<Card> getPlayerCards() {
        return List.copyOf(playerRow().cards());
    }

    TableRow playerRow() {
        return rows.get(Row.PLAYER);
    }

    TableRow opponentRow() {
        return rows.get(Row.OPPONENT);
    }
}
