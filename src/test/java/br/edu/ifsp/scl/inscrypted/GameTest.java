package br.edu.ifsp.scl.inscrypted;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameTest {
    @Test
    @DisplayName("Should give five cards to the player in initial hand")
    void shouldGiveFiveCardsToThePlayerInInitialHand() {
        Game game = new Game();
        game.drawInitialHand();
        assertThat(game.getHand().size()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should have one squirrel card among five initial cards in hand")
    void shouldHaveOneSquirrelCardAmongFiveInitialCardsInHand() {
        Game game = new Game();
        game.drawInitialHand();
        int squirrels = (int) game.getHand().stream()
                .map(Card::getTag)
                .filter("Squirrel"::equals)
                .count();

        assertThat(squirrels).isEqualTo(1);
    }
}
