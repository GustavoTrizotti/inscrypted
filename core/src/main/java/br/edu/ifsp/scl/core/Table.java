package br.edu.ifsp.scl.core;

import java.util.*;

public final class Table {
    private final Map<TableSlot, Card> playerRow = new HashMap<>();
    private final Map<TableSlot, Card> opponentRow = new HashMap<>();

    Optional<Card> getPlayerCardAtSlot(TableSlot slot) {
        return Optional.ofNullable(playerRow.get(slot));
    }

    Optional<Card> getOpponentCardAtSlot(TableSlot slot) {
        return Optional.ofNullable(opponentRow.get(slot));
    }

    void insertCardAtSlot(TableSlot slot, Card card) {
        playerRow.putIfAbsent(slot, card);
    }

    void insertOpponentCardAtSlot(TableSlot slot, Card card) {
        opponentRow.putIfAbsent(slot, card);
    }

    void removeCardAtSlot(TableSlot slot) {
        playerRow.remove(slot);
    }

    void removeOpponentCardAtSlot(TableSlot slot) {
        opponentRow.remove(slot);
    }

    Set<TableSlot> getTableSlots() {
        return Set.copyOf(playerRow.keySet());
    }

    List<Card> getOpponentRow() {
        return List.copyOf(opponentRow.values());
    }

    List<Card> getPlayerRow() {
        return List.copyOf(playerRow.values());
    }
}
