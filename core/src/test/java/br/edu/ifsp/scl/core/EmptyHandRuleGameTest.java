package br.edu.ifsp.scl.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tags.GameTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

@GameTest
public class EmptyHandRuleGameTest {
    @Test
    @DisplayName("Should not allow the player to ring the bell before drawing the initial 5 cards")
    void shouldNotAllowThePlayerToRingTheBellBeforeDrawingTheInitial5Cards() {
        var hand = new Hand();
        var sut = new Game(hand);

        assertThat(hand.getCards().size()).isEqualTo(0);
        assertThatIllegalStateException().isThrownBy(sut::ringBell);
    }
}
