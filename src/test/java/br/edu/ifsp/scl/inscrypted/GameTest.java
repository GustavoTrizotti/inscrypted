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
}
