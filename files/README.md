# 🎮 Java Game Collection

This repository contains two fun Java-based console games:
- **Guess The Number**
- **Rock, Paper, Scissors**

---

## 📄 1. Guess The Number Game

- ### 📚 Description
- **GuessTheNumber.java** is a Java console-based game where the user tries to guess a randomly generated number between 1 and 50.  
- The program provides hints to the user by indicating whether the guessed number is too high or too low.

---

### 🛠️ How to Run
1. **Compile the Java file:**
```bash
javac GuessTheNumber.java
```
- Run the compiled class:
```bash
java GuessTheNumber
```
- 📌 Game Instructions
- You have to guess a number between 1 and 50.
- If your guess is less than the target number, the program will prompt that the guessed number is too low.
- If your guess is greater than the target number, it will indicate that the number is too high.
- You will be awarded stars based on the number of tries:
- ⭐ 1 Star for 13-20 tries.
- ⭐⭐ 2 Stars for 7-12 tries.
- ⭐⭐⭐ 3 Stars for 1-6 tries.
- ❌ No stars if it takes more than 20 tries.
- 🔄 Play Again
- After finishing the game, type yes to play again.
- Type anything else to exit the game.







---

✊ 2. Rock, Paper, Scissors Game

- ### 📚 Description
- RPS.java is a simple Java console game where the user plays Rock, Paper, Scissors against the computer.
- The game randomly selects the computer's choice, and the result is determined based on the standard rules of the game.
  
---

🛠️ How to Run
Compile the Java file:
```bash
javac RPS.java
```

Run the compiled class:
```bash
java RPS
```
📌 Game Instructions
Choose between:
✊ Rock
📄 Paper
✂️ Scissors
The computer will randomly choose one of the options.
The game follows these rules:
Rock beats Scissors.
Scissors beats Paper.
Paper beats Rock.
The winner is displayed after each round.
🔄 Play Again
The user can choose to play multiple rounds.
After finishing, type yes to play again or anything else to quit.
🚀 Additional Notes
Both programs provide a fun and interactive console-based gaming experience.
Make sure to run the files with a compatible Java Development Kit (JDK).

---
