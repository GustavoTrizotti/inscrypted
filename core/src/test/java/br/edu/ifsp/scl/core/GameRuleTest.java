package br.edu.ifsp.scl.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import tags.GameTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@GameTest
public class GameRuleTest extends BaseGameTest {
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

    @ParameterizedTest(name = "For {0} of damage")
    @ValueSource(ints = {1, 2, 3, 4})
    @DisplayName("Should remove opponent's life points when receiving an attack from opposing player card")
    void shouldRemoveOpponentLifePointsWhenReceivingAnAttackFromAdjacentPlayerCard(int damage) {
        Card attacker = new Card("Dummy", Cost.ZERO, damage, 1);
        Game sut = createGameWithOneCard(attacker);
        sut.drawInitialHand();

        sut.select(0);
        sut.placeCard(TableSlot.FIRST);

        sut.ringBell();

        assertThat(attacker.getDamage()).isEqualTo(damage);
        assertThat(sut.getOpponentLife()).isEqualTo(damage * -1);
    }

    @ParameterizedTest(name = "For {0} of damage")
    @ValueSource(ints = {1, 2, 3, 4})
    @DisplayName("Should remove player's life points when receiving an attack from adjacent opponent card")
    void shouldRemovePlayerLifePointsWhenReceivingAnAttackFromAdjacentOpponentCard(int damage) {
        Card opponentCard = new Card("Dummy", Cost.ZERO, damage, 1);

        sut.placeOpponentCardAtSlot(opponentCard, TableSlot.FIRST);
        sut.ringBell();

        assertThat(sut.getCardAtSlot(TableSlot.FIRST)).isEmpty();
        assertThat(sut.getPlayerLife()).isEqualTo(damage * -1);
    }

    @Test
    @DisplayName("Should reduce card life after beign attacked by opposing card")
    void shouldReduceCardLifeAfterBeignAttackedByOpposingCard() {
        Card card = Card.identity(Cost.ZERO);
        Card attacked = Card.identity(Cost.ZERO);

        Game sut = createGameWithOneCard(card);
        sut.drawInitialHand();

        sut.select(0);
        sut.placeCard(TableSlot.FIRST);

        sut.placeOpponentCardAtSlot(attacked, TableSlot.FIRST);

        sut.ringBell();

        assertThat(attacked.getLife()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should remove card that reached zero life points after ringing the bell")
    void shouldRemoveCardThatReachedZeroLifePointsAfterRingingTheBell() {
        Card card = Card.identity(Cost.ZERO);
        Card attacked = Card.identity(Cost.ZERO);
        placeOpposingCardsAndRingBell(card, attacked);

        assertThat(sut.getOpponentCardAtSlot(TableSlot.FIRST)).isEmpty();
    }
}
