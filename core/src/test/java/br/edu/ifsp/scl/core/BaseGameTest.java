package br.edu.ifsp.scl.core;

import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

public class BaseGameTest {
    protected Game sut;

    @BeforeEach
    void setUp() {
        sut = new Game();
        sut.drawInitialHand();
    }

    protected static Game createGameWithOneCard(Card card) {
        var sut = new Game();
        sut.draw(card);
        return sut;
    }

    protected static Game createGameWithCards(Card... cards) {
        var sut = new Game();
        Arrays.stream(cards).forEach(sut::draw);
        return sut;
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
