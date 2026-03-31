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

    public void placeCardAtSlot(Card card, TableSlot tableSlot) {
        Card inSlot = table.get(tableSlot);
        if (inSlot != null) {
            throw new IllegalCardPlacementException("The card \"%s\" is already at position \"%s\"!".formatted(
                    inSlot.getTag(),
                    tableSlot.name()
            ));
        }

        if (!card.isCostReached())
            throw new IllegalCardPlacementException("The card \"%s\" hasn't reached it's sacrifice cost!".formatted(
                    card.getTag()
            ));

        table.putIfAbsent(tableSlot, card);
        cards.remove(card);
    }

    public void sacrifice(Card card, Card sacrificed) {
        card.addSacrifice();
    }

    public Optional<Card> getCardAtSlot(TableSlot tableSlot) {
        return Optional.ofNullable(table.get(tableSlot));
    }
}
