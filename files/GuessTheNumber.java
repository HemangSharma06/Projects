import java.util.Random;
import java.util.Scanner;
class number {
    Scanner sc = new Scanner(System.in);
    int input = sc.nextInt();
}

public class GuessTheNumber {
    public static void main(String[] args) {
        String str = "yes";
        while (str == "yes") {
            System.out.println("Guess any number from 1 to 50\n\n");
            Random rn = new Random();
            int num2 = rn.nextInt(50);
            String x = "y";
            int count = 0;
            while (x == "y") {
                System.out.print("Enter your guess number: ");
                number num3 = new number();
                int num1 = num3.input;
                if (num1 != num2) {
                    if (num1 < num2) {
                        System.out.println("The number you guessed is less than the actual number");
                        count += 1;
                    }
                    if (num1 > num2) {
                        System.out.println("The number you guessed is greater than the actual number");
                        count += 1;
                    }
                } else {
                    if (num1 == num2) {
                        System.out.println("You guessed the actual number");
                        count += 1;
                    }
                    break;
                }
            }
            System.out.println("\n\nYou guessed the number after the " + count + " tries");
            if (count <= 20 && count >=13) {
                System.out.println("\n\nYou get 1 star");
            }
            if (count <= 12 && count >= 7) {
                System.out.println("\n\nYou get 2 stars");
            }
            if (count <= 6 && count >= 1) {
                System.out.println("\n\nYou get 3 stars");
            }
            if (count >= 21) {
                System.out.println("You get 0 star");
            }
            Scanner sc1 = new Scanner(System.in);
            System.out.print("Enter yes in small letters to play again.\nEnter anything to exit.\nEnter the choice: ");
            str = sc1.next();
        }
    }
}