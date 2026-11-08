package br.edu.ifsp.scl.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

final class TableRow {
    private final Map<TableSlot, Card> row = new HashMap<>();

    void insertCardAtSlot(TableSlot slot, Card card) {
        row.put(slot, card);
    }

    void removeCardAtSlot(TableSlot slot) {
        row.remove(slot);
    }

    Optional<Card> getCardAtSlot(TableSlot slot) {
        return Optional.ofNullable(row.get(slot));
    }

    List<Card> cards() {
        return List.copyOf(row.values());
    }
}
