package br.edu.ifsp.scl.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import tags.GameTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIndexOutOfBoundsException;

@GameTest
public class SelectionRuleGameTest extends BaseGameTest {
    @Test
    @DisplayName("Should mark a card as current selected card when selecting it")
    void shouldMarkACardAsCurrentSelectedCardWhenSelectingIt() {
        sut.select(0);
        assertThat(sut.getSelectedCard()).hasValue(sut.getHand().getFirst());
    }

    @Test
    @DisplayName("Should replace the previous card when selecting different card from previous")
    void shouldReplaceThePreviousCardWhenSelectingDifferentCardFromPrevious() {
        sut.select(0);
        Card firstSelected = sut.getSelectedCard().get();
        sut.select(1);
        assertThat(sut.getSelectedCard().get()).isNotEqualTo(firstSelected);
    }

    @Test
    @DisplayName("Should throw IndexOutOfBoundsException if player selects card from inalid index")
    void shouldThrowIndexOutOfBoundsExceptionIfPlayerSelectsCardFromInalidIndex() {
        assertThatIndexOutOfBoundsException()
                .isThrownBy(() -> sut.select(sut.getHand().size()));
    }

    @Test
    @DisplayName("Should allow place a card without cost to be place in any table position")
    void shouldAllowPlaceACardWithoutCostToBePlaceInAnyTablePosition() {
        Card squirrel = new Card("Squirrel", Cost.ZERO, 0, 0);
        Game sut = createGameWithOneCard(squirrel);

        sut.select(0);

        sut.placeCard(TableSlot.FIRST);
        assertThat(sut.getCardAtSlot(TableSlot.FIRST)).hasValue(squirrel);
    }

    @ParameterizedTest(name = "For cost \"{0}\"")
    @EnumSource(value = Cost.class, names = {"ZERO"}, mode = EnumSource.Mode.EXCLUDE)
    @DisplayName("Should prepare sacrifice mode when selecting card with one or more sacrifice cost")
    void shouldPrepareSacrificeModeWhenSelectingCardWithOneOrMoreSacrificeCost(Cost cost) {
        Card card = Card.identity(cost);
        Game sut = createGameWithOneCard(card);

        sut.select(0);

        assertThat(sut.getSelectedCard()).hasValue(card);
        assertThat(sut.isSacrificeMode()).isTrue();
    }

    @Test
    @DisplayName("Should cancel sacrifice mode when changing the selected card")
    void shouldCancelSacrificeModeWhenChangingTheSelectedCard() {
        Card squirrel = Card.createSquirrel();
        Card costly = Card.identity(Cost.ONE);

        Game sut = createGameWithCards(squirrel, costly);
        sut.select(1);
        sut.select(0);

        assertThat(sut.getSelectedCard()).hasValue(squirrel);
        assertThat(sut.isSacrificeMode()).isFalse();
    }
}
