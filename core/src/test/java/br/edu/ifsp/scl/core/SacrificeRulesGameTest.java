package br.edu.ifsp.scl.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tags.GameTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class SacrificeRulesGameTest extends BaseGameTest {
    @Test
    @GameTest
    @DisplayName("Should grant that a placed card is not in player hand after placed")
    void shouldGrantThatAPlacedCardIsNotInPlayerHandAfterPlaced() {
        Card card = sut.getHand().getFirst();
        sut.select(0);

        sut.placeCard(TableSlot.FIRST);

        assertThat(sut.getCardAtSlot(TableSlot.FIRST)).hasValue(card);
        assertThat(sut.getHand().contains(card)).isFalse();
    }

    @Test
    @GameTest
    @DisplayName("Should remove a sacrificed card from the table after sacrificing")
    void shouldRemoveASacrificedCardFromTheTableAfterSacrificing() {
        Card card = Card.identity(Cost.ONE);
        Card sacrificed = Card.identity(Cost.ZERO);
        Game sut = createGameWithCards(sacrificed, card);

        sut.select(0);
        sut.placeCard(TableSlot.FIRST);

        sut.select(0);
        sut.sacrifice(TableSlot.FIRST);

        assertThat(sut.getCardAtSlot(TableSlot.FIRST)).isEmpty();
    }

    @Test
    @GameTest
    @DisplayName("Should throw IllegalSacrificeException if trying to sacrifice a card that isn't in table")
    void shouldThrowIllegalSacrificeExceptionIfTryingToSacrificeACardThatIsntInTable() {
        sut.select(0);

        assertThatExceptionOfType(IllegalSacrificeException.class)
                .isThrownBy(() -> sut.sacrifice(TableSlot.FIRST));
    }

    @Test
    @GameTest
    @DisplayName("Should throw IllegalSacrificeException when adding sacrifices to a zero cost card")
    void shouldThrowIllegalSacrificeExceptionWhenAddingSacrificesToAZeroCostCard() {
        Card zeroCostCard = Card.identity(Cost.ZERO);
        Card toSacrifice = Card.identity(Cost.ZERO);
        Game sut = createGameWithCards(toSacrifice, zeroCostCard);

        sut.select(0);
        sut.placeCard(TableSlot.FIRST);

        sut.select(0);
        assertThatExceptionOfType(IllegalSacrificeException.class)
                .isThrownBy(() -> sut.sacrifice(TableSlot.FIRST));
    }
}
