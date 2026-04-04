package br.edu.ifsp.scl.inscrypted;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
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
    @DisplayName("Should grant that two opponent cards have been placed on table in game start")
    void shouldGrantThatTwoOpponentCardsHaveBeenPlacedOnTableInGameStart() {
        sut.placeInitialOpponentCards();
        assertThat(sut.getOpponentRow().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should not allow the opponent to place a card in player row")
    void shouldNotAllowTheOpponentToPlaceACardInPlayerRow() {
        sut.placeInitialOpponentCards();
        List<Card> opponentRow = sut.getOpponentRow();
        assertThat(sut.getPlayerRow().containsAll(opponentRow)).isFalse();
    }

    @Test
    @DisplayName("Should not allow the opponent to pick a squirrel to be placed in table")
    void shouldNotAllowTheOpponentToPickASquirrelToBePlacedInTable() {
        sut.placeInitialOpponentCards();
        List<Card> opponentRow = sut.getOpponentRow();
        boolean hasSquirrel = opponentRow.stream()
                .anyMatch(card -> card.getTag().equalsIgnoreCase("Squirrel"));
        assertThat(hasSquirrel).isFalse();
    }

    @Test
    @DisplayName("Should not allow player to ring the bell before game places two initial opponent's card")
    void shouldNotAllowPlayerToRingTheBellBeforeGamePlacesTwoInitialOpponentCard() {
        assertThat(sut.getOpponentRow().isEmpty()).isTrue();
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> sut.ringBell());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    @DisplayName("Should remove opponent's life points when receiving an attack from adjacent player card")
    void shouldRemoveOpponentLifePointsWhenReceivingAnAttackFromAdjacentPlayerCard(int damage) {
        Card attacker = mock(Card.class);
        when(attacker.isCostReached()).thenReturn(true);
        when(attacker.getAttack()).thenReturn(damage);

        sut.placeInitialOpponentCards();
        sut.placeCardAtSlot(attacker, TableSlot.FIRST);
        sut.ringBell();
        assertThat(attacker.getAttack()).isEqualTo(damage);
        assertThat(sut.getOpponentCardAtSlot(TableSlot.FIRST)).isEmpty();
        assertThat(sut.getOpponentLife()).isEqualTo(damage * -1);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    @DisplayName("Should remove player's life points when receiving an attack from adjacent opponent card")
    void shouldRemovePlayerLifePointsWhenReceivingAnAttackFromAdjacentOpponentCard(int damage) {
        Card opponentCard = mock(Card.class);

        when(opponentCard.getAttack()).thenReturn(damage);
        sut.placeOpponentCardAtSlot(opponentCard, TableSlot.FIRST);

        sut.ringBell();
        assertThat(sut.getCardAtSlot(TableSlot.FIRST)).isEmpty();
        assertThat(sut.getPlayerLife()).isEqualTo(damage * -1);
    }

    @Nested
    @DisplayName("Before drawing cards to initial hand tests")
    class BeforeDrawingInitialHandTests {
        private Game sut;

        @BeforeEach
        void setup() {
            sut = new Game();
        }

        @Test
        @DisplayName("Should not allow the player to ring the bell before drawing the initial 5 cards")
        void shouldNotAllowThePlayerToRingTheBellBeforeDrawingTheInitial5Cards() {
            assertThat(sut.getHand().size()).isEqualTo(0);
            assertThatIllegalStateException().isThrownBy(() -> sut.ringBell());
        }
    }
}
