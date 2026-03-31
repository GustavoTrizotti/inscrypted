package br.edu.ifsp.scl.inscrypted;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class GameTest {
    private Game sut;

    @BeforeEach
    void setUp() {
        sut = new Game();
        sut.drawInitialHand();
    }

    @Test
    @DisplayName("Should give five cards to the player in initial hand")
    void shouldGiveFiveCardsToThePlayerInInitialHand() {
        assertThat(sut.getHand().size()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should have one squirrel card among five initial cards in hand")
    void shouldHaveOneSquirrelCardAmongFiveInitialCardsInHand() {
        int squirrels = (int) sut.getHand().stream()
                .map(Card::getTag)
                .filter("Squirrel"::equals)
                .count();

        assertThat(squirrels).isEqualTo(1);
    }

    @Test
    @DisplayName("Should allow place a card without cost to be place in any table position")
    void shouldAllowPlaceACardWithoutCostToBePlaceInAnyTablePosition() {
        List<Card> hand = List.of(new Card("Squirrel"));
        Card squirrel = hand.getFirst();
        sut.placeCardAtSlot(squirrel, TableSlot.FIRST);
        assertThat(squirrel.getCost()).isEqualTo(Cost.ZERO);
        assertThat(sut.getCardAtSlot(TableSlot.FIRST)).hasValue(squirrel);
    }

    @Test
    @DisplayName("Should throw InvalidCardPlacementException if placing a card in an occupied table position")
    void shouldThrowInvalidCardPlacementExceptionIfPlacingACardInAnOccupiedTablePosition() {
        Card card = new Card("");
        Card toBePlaced = new Card("");
        sut.placeCardAtSlot(card, TableSlot.FIRST);
        assertThatExceptionOfType(InvalidCardPlacementException.class)
                .isThrownBy(() -> sut.placeCardAtSlot(toBePlaced, TableSlot.FIRST));
    }
}
