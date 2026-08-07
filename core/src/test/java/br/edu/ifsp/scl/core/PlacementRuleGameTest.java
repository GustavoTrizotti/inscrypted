package br.edu.ifsp.scl.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import tags.GameTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class PlacementRuleGameTest extends BaseGameTest {
    @ParameterizedTest(name = "For {0} table slot")
    @GameTest
    @EnumSource(TableSlot.class)
    @DisplayName("Should throw NoCardSelectedException when trying to place the selected card before selecting in any slot")
    void shouldThrowNoCardSelectedExceptionWhenTryingToPlaceTheSelectedCardBeforeSelectingInAnySlot(TableSlot slot) {
        assertThatExceptionOfType(NoCardSelectedException.class)
                .isThrownBy(() -> sut.placeCard(slot));
        assertThat(sut.getSelectedCard()).isEmpty();
    }

    @ParameterizedTest(name = "For {0} table slot")
    @GameTest
    @EnumSource(TableSlot.class)
    @DisplayName("Should throw IllegalCardPlacementException if placing a card in an occupied table position")
    void shouldThrowIllegalCardPlacementExceptionIfPlacingACardInAnOccupiedTablePosition(TableSlot slot) {
        Card card = Card.identity(Cost.ZERO);
        Card toBePlaced = Card.identity(Cost.ZERO);

        Game sut = createGameWithCards(card, toBePlaced);

        sut.select(sut.getHand().indexOf(card));
        sut.placeCard(slot);

        sut.select(sut.getHand().indexOf(toBePlaced));
        assertThatExceptionOfType(IllegalCardPlacementException.class)
                .isThrownBy(() -> sut.placeCard(slot));
    }

    @Test
    @GameTest
    @DisplayName("Should throw UnsatisfiedSacrificeException when placing card without reaching sacrifice cost")
    void shouldThrowUnsatisfiedSacrificeExceptionWhenPlacingCardWithoutReachingSacrificeCost() {
        Card card = Card.identity(Cost.ONE);
        Game sut = createGameWithOneCard(card);
        sut.select(0);

        assertThatExceptionOfType(UnsatisfiedSacrificeException.class)
                .isThrownBy(() -> sut.placeCard(TableSlot.FIRST));
    }

    @Test
    @GameTest
    @DisplayName("Should allow place a card that covered its sacrifice cost in a free table position")
    void shouldAllowPlaceACardThatCoveredItsSacrificeCostInAFreeTablePosition() {
        Card card = Card.identity(Cost.ONE);
        Card sacrificed = Card.identity(Cost.ZERO);

        Game sut = createGameWithCards(sacrificed, card);

        sut.select(0);
        sut.placeCard(TableSlot.FIRST);

        sut.select(0);
        sut.sacrifice(TableSlot.FIRST);
        sut.placeCard(TableSlot.FIRST);

        assertThat(card.isCostReached()).isEqualTo(true);
        assertThat(sut.getCardAtSlot(TableSlot.FIRST)).hasValue(card);
    }
}
