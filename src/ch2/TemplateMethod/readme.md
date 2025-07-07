# Template Method
## Concepts
The routine is predefined in the main function, the differences is hidden in the children.
Forces are 
1. Behavioural Variation (code duplication)
2. Maintainability and Extensibility

![Uploading image.png…]()

## Homework practice
Firstly user choose how many human players and name them, AI got its own name.
Secondly choose the game they want to play.
Game could be showdown game or UNO, which have different rules, different cards...etc. (Behavioural Variation)
Template method in this homework is to free the duplicate "process".
If we will add another game in this homework, we don't need to change from current code. (Maintainability and Extensibility)
