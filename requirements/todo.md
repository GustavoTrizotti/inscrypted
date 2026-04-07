| Completed | ID  | Description                                                                             |
|:---------:|:---:|-----------------------------------------------------------------------------------------|
|     ✓     | S1  | The initial hand must contain five cards                                                |
|     ✓     | S2  | The initial hand must contain exactly one squirrel card                                 |
|     ✓     | S3  | Selecting a card from hand marks it as the current selected card                        |
|           | S4  | Selecting a different card from hand replaces the previous selection                    |
|           | S5  | Selecting an invalid hand index must fail                                               |
|           | S6  | Selecting a zero-cost card prepares it for placement                                    |
|           | S7  | Selecting a costly card enters sacrifice mode                                           |
|           | S8  | Selecting a new hand card while in sacrifice mode cancels the previous sacrifice target |
|           | S9  | A zero-cost selected card can be placed in any free table slot                          |
|           | S10 | A selected card cannot be placed in an occupied table slot                              |
|           | S11 | A selected costly card cannot be placed before enough sacrifices                        |
|           | S12 | A selected costly card can be placed after covering its sacrifice cost                  |
|           | S13 | A successfully placed card must be removed from the player's hand                       |
|           | S14 | Placing a selected zero-cost card clears the current selection                          |
|           | S15 | Placing a selected costly card clears the current selection                             |
|           | S16 | Selecting a table card while in sacrifice mode adds one sacrifice                       |
|           | S17 | Selecting a table card while not in sacrifice mode must fail                            |
|           | S18 | A sacrificed card must be removed from the table immediately                            |
|           | S19 | After enough sacrifices the selected costly card must have its cost reached             |
|           | S20 | A table card that does not exist cannot be selected as sacrifice                        |
|           | S21 | A zero-cost selected card cannot receive sacrifices                                     |
|           | S22 | Additional sacrifices cannot be added after the selected card already reached its cost  |
|           | S23 | The opponent setup must place two cards on the opponent row                             |
|           | S24 | The opponent must not place cards in the player row                                     |
|           | S25 | The opponent must not place squirrel cards on the table                                 |
|           | S26 | A card loses life when attacked by an opposing card                                     |
|           | S27 | A card that reaches zero life must be removed from the table after combat resolves      |
|           | S28 | The player cannot ring the bell before drawing the initial hand                         |