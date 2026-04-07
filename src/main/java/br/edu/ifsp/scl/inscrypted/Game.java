package br.edu.ifsp.scl.inscrypted;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Game {
    private final Hand hand;
    private final Map<TableSlot, Card> playerRow = new HashMap<>();
    private final Map<TableSlot, Card> opponentRow = new HashMap<>();

    private int playerLife, opponentLife;

    public Game(Hand hand) {
        this.hand = hand;
    }

    public void drawInitialHand() {
        drawToPlayer();
    }

    private void drawToPlayer() {
        hand.draw(Card.createSquirrel());
        for (int i = 0; i < 4; i++) {
            hand.draw(Card.identity(Cost.ZERO));
        }
    }

    public List<Card> getHand() {
        return hand.getCards();
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
        hand.remove(card);
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

    public void placeInitialOpponentCards() {
        Card first = Card.identity(Cost.ZERO);
        Card second = Card.identity(Cost.ZERO);
        placeOpponentCardAtSlot(first, TableSlot.FIRST);
        placeOpponentCardAtSlot(second, TableSlot.SECOND);
    }

    public List<Card> getOpponentRow() {
        return List.copyOf(opponentRow.values());
    }

    public List<Card> getPlayerRow() {
        return List.copyOf(playerRow.values());
    }

    public void ringBell() {
        if (hand.getCards().isEmpty())
            throw new IllegalStateException("Cannot ring the bell before game starts");

        playerRow.forEach((slot, card) -> {
            Optional<Card> opponentCardAtSlot = getOpponentCardAtSlot(slot);
            if (opponentCardAtSlot.isEmpty()) {
                opponentLife -= card.getDamage();
            } else {
                Card opponentCard = opponentCardAtSlot.get();
                opponentCard.reduceLifeBy(card.getDamage());
                if (opponentCard.getLife() <= 0) {
                    opponentRow.remove(slot);
                }
            }
        });

        opponentRow.forEach((slot, card) -> {
            Optional<Card> cardAtSlot = getCardAtSlot(slot);
            if (cardAtSlot.isEmpty()) {
                playerLife -= card.getDamage();
            } else {
                Card opposingCard = cardAtSlot.get();
                opposingCard.reduceLifeBy(card.getDamage());
                if (opposingCard.getLife() <= 0) {
                    playerRow.remove(slot);
                }
            }
        });
    }

    public Optional<Card> getOpponentCardAtSlot(TableSlot tableSlot) {
        return Optional.ofNullable(opponentRow.get(tableSlot));
    }

    public int getOpponentLife() {
        return opponentLife;
    }

    public int getPlayerLife() {
        return playerLife;
    }

    public void select(int cardIndex) {
    }

    public Optional<Card> getSelectedCard() {
        return Optional.ofNullable(hand.getCards().getFirst());
    }
}
