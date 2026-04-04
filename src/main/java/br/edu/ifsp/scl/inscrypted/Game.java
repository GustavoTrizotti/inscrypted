package br.edu.ifsp.scl.inscrypted;

import java.util.*;

public class Game {
    private final List<Card> cards = new ArrayList<>();
    private final List<Card> opponentHand = new ArrayList<>();

    private final Map<TableSlot, Card> playerRow = new HashMap<>();
    private final Map<TableSlot, Card> opponentRow = new HashMap<>();

    private int playerLife, opponentLife;

    public void drawInitialHand() {
        drawToPlayer();
        drawToOpponent();
    }

    private void drawToPlayer() {
        cards.add(new Card("Squirrel"));
        for (int i = 0; i < 4; i++) {
            cards.add(new Card(""));
        }
    }

    private void drawToOpponent() {
        for (int i = 0; i < 5; i++) {
            opponentHand.add(new Card(""));
        }
    }

    public List<Card> getHand() {
        return List.copyOf(cards);
    }

    public void placeCardAtSlot(Card card, TableSlot tableSlot) {
        Card inSlot = playerRow.get(tableSlot);
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

        playerRow.putIfAbsent(tableSlot, card);
        cards.remove(card);
    }

    void placeOpponentCardAtSlot(Card card, TableSlot slot) {
        opponentRow.put(slot, card);
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
        playerRow.remove(sacrificeSlot);
    }

    public Optional<Card> getCardAtSlot(TableSlot tableSlot) {
        return Optional.ofNullable(playerRow.get(tableSlot));
    }

    public List<Card> getOpponentHand() {
        return List.copyOf(opponentHand);
    }

    public void placeInitialOpponentCards() {
        Collections.shuffle(opponentHand);
        for (int i = 0; i < 2; i++) {
            Card card = opponentHand.remove(i);
            TableSlot slot = getSlotAtIndex(i);
            opponentRow.put(slot, card);
        }
    }

    private TableSlot getSlotAtIndex(int index) {
        if (index == 0) return TableSlot.FIRST;
        if (index == 1) return TableSlot.SECOND;
        if (index == 2) return TableSlot.THIRD;
        if (index == 3) return TableSlot.FOURTH;
        throw new IllegalArgumentException("Invalid index for table slot!");
    }

    public List<Card> getOpponentRow() {
        return List.copyOf(opponentRow.values());
    }

    public List<Card> getPlayerRow() {
        return List.of();
    }

    public void ringBell() {
        if (getHand().isEmpty() || getOpponentRow().isEmpty())
            throw new IllegalStateException("Cannot ring the bell before game starts");

        playerRow.forEach((slot, card) -> {
            if (getOpponentCardAtSlot(slot).isEmpty()) {
                opponentLife -= card.getAttack();
            }
        });

        opponentRow.forEach((slot, card) -> {
            if (getCardAtSlot(slot).isEmpty()) {
                playerLife -= card.getAttack();
            }
        });
    }

    public Optional<Card> getOpponentCardAtSlot(TableSlot tableSlot) {
        return Optional.empty();
    }

    public int getOpponentLife() {
        return opponentLife;
    }

    public int getPlayerLife() {
        return playerLife;
    }
}
