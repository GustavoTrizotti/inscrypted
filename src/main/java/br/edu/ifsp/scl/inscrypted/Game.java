package br.edu.ifsp.scl.inscrypted;

import java.util.*;

public class Game {
    private final List<Card> cards = new ArrayList<>();
    private final Map<TableSlot, Card> table = new HashMap<>();

    public void drawInitialHand() {
        cards.add(new Card("Squirrel"));
        for (int i = 0; i < 4; i++) {
            cards.add(new Card(""));
        }
    }

    public List<Card> getHand() {
        return List.copyOf(cards);
    }

    public void placeCardAtSlot(Card squirrel, TableSlot tableSlot) {
        table.putIfAbsent(tableSlot, squirrel);
    }

    public Optional<Card> getCardAtSlot(TableSlot tableSlot) {
        return Optional.ofNullable(table.get(tableSlot));
    }
}
