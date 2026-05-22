package br.edu.ifsp.scl.inscrypted;

import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

public class BaseGameTest {
    protected Game sut;

    @BeforeEach
    void setUp() {
        Hand hand = new Hand();
        sut = new Game(hand);
        sut.drawInitialHand();
    }

    protected static Game createGameWithOneCard(Card card) {
        Hand hand = new Hand();
        hand.draw(card);
        return new Game(hand);
    }

    protected static Game createGameWithCards(Card... cards) {
        Hand hand = new Hand();
        Arrays.stream(cards).forEach(hand::draw);
        return new Game(hand);
    }

    protected void placeOpposingCardsAndRingBell(Card card, Card attacked) {
        placeOpposingCards(card, attacked);
        sut.ringBell();
    }

    protected void placeOpposingCards(Card card, Card attacked) {
        sut.placeCardAtSlot(card, TableSlot.FIRST);
        sut.placeOpponentCardAtSlot(attacked, TableSlot.FIRST);
    }
}
