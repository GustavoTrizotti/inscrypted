package br.edu.ifsp.scl.core;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Game {
    private final Hand hand;
    private final Table table;

    private int playerLife, opponentLife;
    private boolean sacrificeMode = false;
    private Card selectedCard;

    public Game() {
        this(new Hand());
    }

    Game(Hand hand) {
        this.hand = hand;
        this.table = new Table();
    }

    public void draw(Card card) {
        hand.draw(card);
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
        var inSlot = table.getPlayerCardAtSlot(tableSlot);

        if (inSlot.isPresent()) {
            throw new IllegalCardPlacementException("The card \"%s\" is already at position \"%s\"!".formatted(
                    inSlot.get().getTag(),
                    tableSlot.name()
            ));
        }

        if (!card.isCostReached())
            throw new UnsatisfiedSacrificeException("The card \"%s\" hasn't reached it's sacrifice cost!".formatted(
                    card.getTag()
            ));

        table.insertCardAtSlot(tableSlot, card);
        hand.remove(card);
    }

    void placeOpponentCardAtSlot(Card card, TableSlot slot) {
        table.insertOpponentCardAtSlot(slot, card);
    }

    public void sacrifice(TableSlot sacrificeSlot) {
        Card selectedCard = getSelectedCard()
                .orElseThrow(() -> new IllegalSacrificeException("No cards selected to add sacrifices!"));

        Optional<Card> cardAtSlot = getCardAtSlot(sacrificeSlot);
        if (cardAtSlot.isEmpty()) {
            throw new IllegalSacrificeException(
                    "Slot \"%s\" hasn't any card to be sacrificed!".formatted(sacrificeSlot.name())
            );
        }

        if (selectedCard.getCost() == Cost.ZERO)
            throw new IllegalSacrificeException("Cannot add sacrifices to a zero cost card!");

        selectedCard.addSacrifice();
        table.removeCardAtSlot(sacrificeSlot);
    }

    public Optional<Card> getCardAtSlot(TableSlot tableSlot) {
        return table.getPlayerCardAtSlot(tableSlot);
    }

    public void placeInitialOpponentCards() {
        Card first = Card.identity(Cost.ZERO);
        Card second = Card.identity(Cost.ZERO);
        table.insertOpponentCardAtSlot(TableSlot.FIRST, first);
        table.insertOpponentCardAtSlot(TableSlot.SECOND, second);
    }

    public List<Card> getOpponentRow() {
        return table.getOpponentRow();
    }

    public List<Card> getPlayerRow() {
        return table.getPlayerRow();
    }

    private void calculateDamagePerSlot(TableSlot slot, boolean isCalculatingForOpponent) {
        var attackingCard = table.getPlayerCardAtSlot(slot);
        var opposingCard = table.getOpponentCardAtSlot(slot);

        if (isCalculatingForOpponent) {
            attackingCard = table.getOpponentCardAtSlot(slot);
            opposingCard = table.getPlayerCardAtSlot(slot);
        }

        if (attackingCard.isEmpty()) return;

        final Card attacker = attackingCard.get();

        if (opposingCard.isEmpty() && isCalculatingForOpponent) {
            playerLife -= attacker.getDamage();
            return;
        }

        if (opposingCard.isEmpty()) {
            opponentLife -= attacker.getDamage();
            return;
        }

        opposingCard.get().reduceLifeBy(attacker.getDamage());

        if (!opposingCard.get().isDead()) return;

        if (isCalculatingForOpponent) {
            table.removeCardAtSlot(slot);
            return;
        }

        table.removeOpponentCardAtSlot(slot);
    }

    private void calculatePlayerDamagePerSlot(TableSlot slot) {
        calculateDamagePerSlot(slot, false);
    }

    private void calculateOpponentDamagePerSlot(TableSlot slot) {
        calculateDamagePerSlot(slot, true);
    }

    public void ringBell() {
        if (hand.getCards().isEmpty())
            throw new IllegalStateException("Cannot ring the bell before game starts");

        final Set<TableSlot> slots = TableSlot.set();

        slots.forEach(this::calculatePlayerDamagePerSlot);
        slots.forEach(this::calculateOpponentDamagePerSlot);
    }

    public Optional<Card> getOpponentCardAtSlot(TableSlot tableSlot) {
        return table.getOpponentCardAtSlot(tableSlot);
    }

    public int getOpponentLife() {
        return opponentLife;
    }

    public int getPlayerLife() {
        return playerLife;
    }

    public void select(int cardIndex) {
        selectedCard = hand.getCards().get(cardIndex);
        sacrificeMode = selectedCard.getCost() != Cost.ZERO;
    }

    public Optional<Card> getSelectedCard() {
        return Optional.ofNullable(selectedCard);
    }

    public void placeCard(TableSlot slot) {
        if (selectedCard == null)
            throw new NoCardSelectedException("No card selected to place!");

        placeCardAtSlot(selectedCard, slot);
    }

    public boolean isSacrificeMode() {
        return sacrificeMode;
    }
}
