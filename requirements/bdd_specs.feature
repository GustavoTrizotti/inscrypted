Feature: Inscryption game requirements
  As a game domain
  I want the rules of hand drawing, selection, placement, sacrifice, opponent setup and combat
  So that the match behaves like the intended Inscryption-inspired flow

  Rule: Initial hand rules

    Scenario: The initial hand must contain five cards
      GIVEN the game is created
        AND the player draws the initial hand
      WHEN checking the number of cards in the player's hand
      THEN the result must be 5

    Scenario: The initial hand must contain exactly one squirrel card
      GIVEN the game is created
        AND the player draws the initial hand
      WHEN counting the cards tagged as "Squirrel" in the player's hand
      THEN the result must be 1

  Rule: Card selection rules

    Scenario: Selecting a card from hand marks it as the current selected card
      GIVEN the game is created
        AND the player draws the initial hand
      WHEN the player selects the card at hand index 0
      THEN that card must become the current selected card

    Scenario: Selecting a different card from hand replaces the previous selection
      GIVEN the game is created
        AND the player draws the initial hand
        AND the player selected the card at hand index 0
      WHEN the player selects the card at hand index 1
      THEN the card at hand index 1 must become the current selected card
        AND the card at hand index 0 must no longer be selected

    Scenario: Selecting an invalid hand index must fail
      GIVEN the game is created
        AND the player draws the initial hand
      WHEN the player selects a hand index that does not exist
      THEN an IndexOutOfBoundsException must be thrown

    Scenario: Selecting a zero-cost card prepares it for placement
      GIVEN the game is created
        AND the player draws the initial hand
        AND the card at hand index 0 has cost ZERO
      WHEN the player selects the card at hand index 0
      THEN the selected card must be ready for placement
        AND the game must not enter sacrifice mode

    Scenario: Selecting a costly card enters sacrifice mode
      GIVEN the game is created
        AND the player draws the initial hand
        AND the card at hand index 0 has cost ONE
      WHEN the player selects the card at hand index 0
      THEN that card must become the current selected card
        AND the game must enter sacrifice mode

    Scenario: Selecting a new hand card while in sacrifice mode cancels the previous sacrifice target
      GIVEN the game is created
        AND the player draws the initial hand
        AND the player selected a hand card with cost ONE
      WHEN the player selects another card from hand
      THEN the newly selected card must become the current selected card
        AND the previous card must no longer be the sacrifice target

  Rule: Placement rules

    Scenario: A zero-cost selected card can be placed in any free table slot
      GIVEN the game is created
        AND the player draws the initial hand
        AND a zero-cost squirrel card exists in hand
        AND the player selects that squirrel card
      WHEN the player places the selected card in the FIRST table slot
      THEN the card cost must be ZERO
        AND the FIRST table slot must contain that card

    Scenario: A selected card cannot be placed in an occupied table slot
      GIVEN the game is created
        AND the player draws the initial hand
        AND a zero-cost card is already placed in the FIRST table slot
        AND another zero-cost card is selected from hand
      WHEN the player tries to place the selected card in the FIRST table slot
      THEN an IllegalCardPlacementException must be thrown

    Scenario: A selected costly card cannot be placed before enough sacrifices
      GIVEN the game is created
        AND the player draws the initial hand
        AND the player selected a hand card with cost ONE
      WHEN the player tries to place the selected card in the FIRST table slot
      THEN an IllegalCardPlacementException must be thrown

    Scenario: A selected costly card can be placed after covering its sacrifice cost
      GIVEN the game is created
        AND the player draws the initial hand
        AND a zero-cost card is placed in the FIRST table slot
        AND the player selected a hand card with cost ONE
        AND the player selected the card in the FIRST table slot as sacrifice
      WHEN the player places the selected card in the FIRST table slot
      THEN the selected card must have its cost reached
        AND the FIRST table slot must contain the selected card

    Scenario: A successfully placed card must be removed from the player's hand
      GIVEN the game is created
        AND the player draws the initial hand
        AND the player selected a card from hand
      WHEN the player places the selected card in the FIRST table slot
      THEN the player's hand must no longer contain that card

    Scenario: Placing a selected zero-cost card clears the current selection
      GIVEN the game is created
        AND the player draws the initial hand
        AND the player selected a zero-cost hand card
      WHEN the player places the selected card in the FIRST table slot
      THEN the FIRST table slot must contain that card
        AND no card must remain selected

    Scenario: Placing a selected costly card clears the current selection
      GIVEN the game is created
        AND the player draws the initial hand
        AND a zero-cost card is placed in the FIRST table slot
        AND the player selected a hand card with cost ONE
        AND the player selected the card in the FIRST table slot as sacrifice
      WHEN the player places the selected card in the FIRST table slot
      THEN the FIRST table slot must contain that card
        AND no card must remain selected

  Rule: Sacrifice rules

    Scenario: Selecting a table card while in sacrifice mode adds one sacrifice
      GIVEN the game is created
        AND the player draws the initial hand
        AND a zero-cost card is placed in the FIRST table slot
        AND the player selected a hand card with cost ONE
      WHEN the player selects the card in the FIRST table slot as sacrifice
      THEN one sacrifice must be added to the selected hand card
        AND the FIRST table slot must become empty

    Scenario: Selecting a table card while not in sacrifice mode must fail
      GIVEN the game is created
        AND the player draws the initial hand
        AND a zero-cost card is placed in the FIRST table slot
        AND no costly card is currently selected
      WHEN the player selects the card in the FIRST table slot as sacrifice
      THEN an IllegalSacrificeException must be thrown

    Scenario: A sacrificed card must be removed from the table immediately
      GIVEN the game is created
        AND the player draws the initial hand
        AND a zero-cost card is placed in the FIRST table slot
        AND the player selected a hand card with cost ONE
      WHEN the player selects the card in the FIRST table slot as sacrifice
      THEN the FIRST table slot must be empty

    Scenario: After enough sacrifices the selected costly card must have its cost reached
      GIVEN the game is created
        AND the player draws the initial hand
        AND a zero-cost card is placed in the FIRST table slot
        AND the player selected a hand card with cost ONE
      WHEN the player selects the card in the FIRST table slot as sacrifice
      THEN the selected hand card must have its cost reached

    Scenario: A table card that does not exist cannot be selected as sacrifice
      GIVEN the game is created
        AND the player draws the initial hand
        AND the player selected a hand card with cost ONE
      WHEN the player selects an empty player table slot as sacrifice
      THEN an IllegalSacrificeException must be thrown

    Scenario: A zero-cost selected card cannot receive sacrifices
      GIVEN the game is created
        AND the player draws the initial hand
        AND a zero-cost card is placed in the FIRST table slot
        AND the player selected a zero-cost hand card
      WHEN the player selects the card in the FIRST table slot as sacrifice
      THEN an IllegalSacrificeException must be thrown

    Scenario: Additional sacrifices cannot be added after the selected card already reached its cost
      GIVEN the game is created
        AND the player draws the initial hand
        AND a zero-cost card is placed in the FIRST table slot
        AND another zero-cost card is placed in the SECOND table slot
        AND the player selected a hand card with cost ONE
        AND the player selected the card in the FIRST table slot as sacrifice
      WHEN the player selects the card in the SECOND table slot as sacrifice
      THEN an IllegalSacrificeException must be thrown

  Rule: Opponent setup rules

    Scenario: The opponent setup must place two cards on the opponent row
      GIVEN the game is created
        AND the player draws the initial hand
      WHEN the initial opponent cards are placed
      THEN the opponent row must contain 2 cards

    Scenario: The opponent must not place cards in the player row
      GIVEN the game is created
        AND the player draws the initial hand
        AND the initial opponent cards are placed
      WHEN comparing the opponent row with the player row
      THEN the player row must not contain all opponent cards

    Scenario: The opponent must not place squirrel cards on the table
      GIVEN the game is created
        AND the player draws the initial hand
        AND the initial opponent cards are placed
      WHEN checking the opponent row cards
      THEN none of them must have the tag "Squirrel"

  Rule: Combat rules

    Scenario Outline: The opponent life points are reduced when attacked by an adjacent player card
      GIVEN the game is created
        AND the player draws the initial hand
        AND a player card with <damage> attack and zero cost is placed in the FIRST table slot
      WHEN the bell is rung
      THEN the attacker damage must be <damage>
        AND the opponent life must be <opponent_life>

      Examples:
        | damage | opponent_life |
        | 1      | -1            |
        | 2      | -2            |
        | 3      | -3            |
        | 4      | -4            |

    Scenario Outline: The player life points are reduced when attacked by an adjacent opponent card
      GIVEN the game is created
        AND the player draws the initial hand
        AND an opponent card with <damage> attack and zero cost is placed in the FIRST opponent slot
      WHEN the bell is rung
      THEN the player's FIRST table slot must be empty
        AND the player life must be <player_life>

      Examples:
        | damage | player_life |
        | 1      | -1          |
        | 2      | -2          |
        | 3      | -3          |
        | 4      | -4          |

    Scenario: A card loses life when attacked by an opposing card
      GIVEN the game is created
        AND the player draws the initial hand
        AND a player card is placed in the FIRST table slot
        AND an opposing card is placed in the FIRST opponent slot
      WHEN the bell is rung
      THEN the attacked card life must be 0

    Scenario: A card that reaches zero life must be removed from the table after combat resolves
      GIVEN the game is created
        AND the player draws the initial hand
        AND a player card is placed in the FIRST table slot
        AND an opposing card is placed in the FIRST opponent slot
      WHEN the bell is rung
      THEN the opponent card in the FIRST slot must be removed from the table

  Rule: Bell precondition rules

    Scenario: The player cannot ring the bell before drawing the initial hand
      GIVEN the game is created
        AND the player has not drawn the initial hand
      WHEN the player tries to ring the bell
      THEN the player's hand must contain 0 cards
        AND an IllegalStateException must be thrown
