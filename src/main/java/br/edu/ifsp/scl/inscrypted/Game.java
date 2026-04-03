package br.edu.ifsp.scl.inscrypted;

import java.util.*;

public class Game {
    private final List<Card> cards = new ArrayList<>();
    private final List<Card> opponentHand = new ArrayList<>();
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

    public void sacrifice(Card card, TableSlot sacrificeSlot) {
        Optional<Card> cardAtSlot = getCardAtSlot(sacrificeSlot);
        if (cardAtSlot.isEmpty()) {
            throw new IllegalSacrificeException(
                    "Slot \"%s\" hasn't any card to be sacrificed!".formatted(sacrificeSlot.name())
            );
        }

        if (card.getCost() == Cost.ZERO)
            throw new IllegalSacrificeException("Cannot add sacrifices to a zero cost card!");

        card.addSacrifice();
        table.remove(sacrificeSlot);
    }

    public Optional<Card> getCardAtSlot(TableSlot tableSlot) {
        return Optional.ofNullable(table.get(tableSlot));
    }

    public List<Card> getOpponentHand() {
        for (int i = 0; i < 5; i++) {
            opponentHand.add(new Card(""));
        }
        return opponentHand;
    }

    public void placeInitialOpponentCards() {

    }

    public List<Card> getOpponentRow() {
        Collections.shuffle(opponentHand);
        return opponentHand.subList(0, 1);
    }
}
