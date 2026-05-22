package br.edu.ifsp.scl.inscrypted;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tags.GameTest;

import static org.assertj.core.api.Assertions.assertThat;

public class HandRuleGameTest extends BaseGameTest {
    @Test
    @GameTest
    @DisplayName("Should give five cards to the player in initial hand")
    void shouldGiveFiveCardsToThePlayerInInitialHand() {
        assertThat(sut.getHand().size()).isEqualTo(5);
    }

    @Test
    @GameTest
    @DisplayName("Should have one squirrel card among five initial cards in hand")
    void shouldHaveOneSquirrelCardAmongFiveInitialCardsInHand() {
        int squirrels = (int) sut.getHand().stream()
                .map(Card::getTag)
                .filter("Squirrel"::equals)
                .count();

        assertThat(squirrels).isEqualTo(1);
    }
}
