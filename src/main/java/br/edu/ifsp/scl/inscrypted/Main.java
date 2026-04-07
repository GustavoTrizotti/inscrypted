package br.edu.ifsp.scl.inscrypted;

import java.util.List;
import java.util.Optional;

public class Main {
    static void main() {
        Hand hand = new Hand();
        Game game = new Game(hand);
        game.drawInitialHand();

        showHand(game);

        game.placeInitialOpponentCards();

        showTable(game);
        Card wolf = new Card("Wolf", Cost.ONE, 3, 2);
        Card squirrel = game.getHand().getFirst();
        game.placeCardAtSlot(squirrel, TableSlot.FIRST);

        showTable(game);

        game.sacrifice(wolf, TableSlot.FIRST);
        game.placeCardAtSlot(wolf, TableSlot.FOURTH);

        showTable(game);

        game.ringBell();

        showTable(game);

        System.out.println(game.getPlayerLife());
        System.out.println(game.getOpponentLife());
    }

    private static void showHand(Game game) {
        List<String[]> renderedCards = game.getHand().stream()
                .map(Card::toString)
                .map(card -> card.split("\n"))
                .toList();

        if (renderedCards.isEmpty()) {
            System.out.println("(empty hand)");
            return;
        }

        int cardHeight = renderedCards.getFirst().length;

        StringBuilder handBuilder = new StringBuilder();

        for (int line = 0; line < cardHeight; line++) {
            for (String[] cardLines : renderedCards) {
                handBuilder.append(cardLines[line]).append("   ");
            }
            handBuilder.append("\n");
        }

        System.out.print(handBuilder);
    }

    private static final int COLS = 4;
    private static final int COL_WIDTH = 22;

    private static void showTable(Game game) {
        StringBuilder stringBuilder = new StringBuilder();

        System.out.println("\nTable\n");

        showOpponentRow(game, stringBuilder);
        stringBuilder.append("\n");
        stringBuilder.repeat("-", (COL_WIDTH + 3) * COLS).append("\n");
        showPlayerRow(game, stringBuilder);

        System.out.println(stringBuilder);
    }

    private static void showOpponentRow(Game game, StringBuilder stringBuilder) {
        for (TableSlot slot : TableSlot.values()) {
            String cell;
            Optional<Card> cardOptional = game.getOpponentCardAtSlot(slot);
            if (cardOptional.isEmpty()) {
                cell = "Empty";
            } else {
                Card obtainedCard = cardOptional.get();
                cell = "%s (%d / %d)".formatted(
                        obtainedCard.getTag(),
                        obtainedCard.getDamage(),
                        obtainedCard.getLife()
                );
            }
            stringBuilder.append(String.format("%-" + COL_WIDTH + "s | ", cell));
        }
    }

    private static void showPlayerRow(Game game, StringBuilder stringBuilder) {
        for (TableSlot slot : TableSlot.values()) {
            String cell;
            Optional<Card> cardOptional = game.getCardAtSlot(slot);
            if (cardOptional.isEmpty()) {
                cell = "Empty";
            } else {
                Card obtainedCard = cardOptional.get();
                cell = "%s (%d / %d)".formatted(
                        obtainedCard.getTag(),
                        obtainedCard.getDamage(),
                        obtainedCard.getLife()
                );
            }
            stringBuilder.append(String.format("%-" + COL_WIDTH + "s | ", cell));
        }
    }
}