package br.edu.ifsp.scl.inscrypted;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    @DisplayName("Should throw IllegalCardPlacementException if placing a card in an occupied table position")
    void shouldThrowIllegalCardPlacementExceptionIfPlacingACardInAnOccupiedTablePosition() {
        Card card = new Card("");
        Card toBePlaced = new Card("");
        sut.placeCardAtSlot(card, TableSlot.FIRST);
        assertThatExceptionOfType(IllegalCardPlacementException.class)
                .isThrownBy(() -> sut.placeCardAtSlot(toBePlaced, TableSlot.FIRST));
    }

    @Test
    @DisplayName("Should throw IllegalCardPlacementException when placing card without reaching sacrifice cost")
    void shouldThrowIllegalCardPlacementExceptionWhenPlacingCardWithoutReachingSacrificeCost() {
        Card card = mock(Card.class);
        when(card.getCost()).thenReturn(Cost.ONE);
        assertThat(card.getCost()).isNotEqualTo(Cost.ZERO);
        assertThatExceptionOfType(IllegalCardPlacementException.class)
                .isThrownBy(() -> sut.placeCardAtSlot(card, TableSlot.FIRST));
    }

    @Test
    @DisplayName("Should allow place a card that covered its sacrifice cost in a free table position")
    void shouldAllowPlaceACardThatCoveredItsSacrificeCostInAFreeTablePosition() {
        Card card = new Card("", Cost.ONE);
        Card sacrificed = new Card("");
        sut.placeCardAtSlot(sacrificed, TableSlot.FIRST);
        sut.sacrifice(card, TableSlot.FIRST);
        sut.placeCardAtSlot(card, TableSlot.FIRST);
        assertThat(card.isCostReached()).isEqualTo(true);
        assertThat(sut.getCardAtSlot(TableSlot.FIRST)).hasValue(card);
    }

    @Test
    @DisplayName("Should grant that a placed card is not in player hand after placed")
    void shouldGrantThatAPlacedCardIsNotInPlayerHandAfterPlaced() {
        Card card = sut.getHand().getFirst();
        sut.placeCardAtSlot(card, TableSlot.FIRST);
        assertThat(sut.getHand().contains(card)).isFalse();
    }

    @Test
    @DisplayName("Should verifiy if a placed card was in players hand before placing")
    void shouldVerifiyIfAPlacedCardWasInPlayersHandBeforePlacing() {
        List<Card> hand = sut.getHand();
        Card card = sut.getHand().getFirst();
        sut.placeCardAtSlot(card, TableSlot.FIRST);
        assertThat(hand.contains(card)).isTrue();
    }

    @Test
    @DisplayName("Should remove a sacrificed card from the table after sacrificing")
    void shouldRemoveASacrificedCardFromTheTableAfterSacrificing() {
        Card card = new Card("", Cost.ONE);
        Card sacrificed = new Card("");
        sut.placeCardAtSlot(sacrificed, TableSlot.FIRST);
        sut.sacrifice(card, TableSlot.FIRST);
        assertThat(sut.getCardAtSlot(TableSlot.FIRST)).isEmpty();
    }

    @Test
    @DisplayName("Should throw IllegalSacrificeException if trying to sacrifice a card that isn't in table")
    void shouldThrowIllegalSacrificeExceptionIfTryingToSacrificeACardThatIsntInTable() {
        Card first = sut.getHand().getFirst();
        assertThatExceptionOfType(IllegalSacrificeException.class)
                .isThrownBy(() -> sut.sacrifice(first, TableSlot.FIRST));
    }

    @Test
    @DisplayName("Should throw IllegalSacrificeException when adding sacrifices to a zero cost card")
    void shouldThrowIllegalSacrificeExceptionWhenAddingSacrificesToAZeroCostCard() {
        Card zeroCostCard = new Card("");
        Card toSacrifice = new Card("");
        sut.placeCardAtSlot(toSacrifice, TableSlot.FIRST);
        assertThatExceptionOfType(IllegalSacrificeException.class)
                .isThrownBy(() -> sut.sacrifice(zeroCostCard, TableSlot.FIRST));
    }

    @Test
    @DisplayName("Should grant that an opponent exists in game creation")
    void shouldGrantThatAnOpponentExistsInGameCreation() {
        assertThat(sut.getOpponentHand()).isNotNull();
    }

    @Test
    @DisplayName("Should grant that opponent has 5 cards before game starts")
    void shouldGrantThatOpponentHas5CardsBeforeGameStarts() {
        List<Card> opponentHand = sut.getOpponentHand();
        assertThat(opponentHand.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should grant that two opponent cards have been placed on table in game start")
    void shouldGrantThatTwoOpponentCardsHaveBeenPlacedOnTableInGameStart() {
        sut.placeInitialOpponentCards();
        assertThat(sut.getOpponentRow().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should grant that the two opponent cards were in his hand before placing")
    void shouldGrantThatTheTwoOpponentCardsWereInHisHandBeforePlacing() {
        List<Card> opponentHand = sut.getOpponentHand();
        sut.placeInitialOpponentCards();
        List<Card> opponentRow = sut.getOpponentRow();
        assertThat(opponentHand.containsAll(opponentRow)).isTrue();
    }
}
