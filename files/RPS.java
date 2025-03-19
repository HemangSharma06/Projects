import java.util.Scanner;
import java.util.Random;

public class RPS {
    public static void main(String[] args) {

        System.out.print("0 -> Rock\n1 -> Paper\n2 -> Sissor\n");
        Scanner sc = new Scanner (System.in);
        Random r = new Random();

        int j;
        int count1 = 0;
        System.out.println("There are 5 rounds.\n");
        int count2 = 1;

        for (j=0;j<5;j++){
            System.out.println(count2 + " Round.....\n");
            int count = 0;

            int i = 5;

            while (i > 0) {

                System.out.print("Enter your choice: ");
                int user_input = sc.nextInt();          // Taking input from user

                int comp_input = r.nextInt(3);  // Taking input from computer

                if (comp_input == user_input) {
                    System.out.println("tie");
                    i++;
                }

                if (comp_input == 0 && user_input == 1) {
                    System.out.println("Congratulations, you won....");
                    count += 1;
                }

                if (comp_input == 0 && user_input == 2) {
                    System.out.println("You lost...");
                }

                if (comp_input == 1 && user_input == 0) {
                    System.out.println("you lost...");
                }

                if (comp_input == 1 && user_input == 2) {
                    System.out.println("Congratulations,you won....");
                    count += 1;
                }

                if (comp_input == 2 && user_input == 0) {
                    System.out.println("Congratulations,you won....");
                    count += 1;
                }

                if (comp_input == 2 && user_input == 1) {
                    System.out.println("you lost...");
                }

                i -= 1;
            }

            if (count >= 3) {
                System.out.println("\n\nCongratulations...., You won the round!!\n\n");
                count1 += 1;
            }

            if (count < 3) {
                System.out.println("\n\nYou lost the round!\n\n");
            }
            count2++;
        }
        if (count1 >= 3){
            System.out.println("\n\nYou won "+ count1 + " out of 5 rounds\n\n"+ "Congratulations...., You won the match!!\n\n");
        }
        if (count1 < 3){
            System.out.println("\n\nYou lost "+(5 - count1) + " out of 5 rounds\n\n" + "You lost the match!\n\n");
        }
    }
}
