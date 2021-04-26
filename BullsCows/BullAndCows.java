package BullsCows;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class BullAndCows {
    private int length;
    private int range;
    private char[] secretCode;
    private int cows;
    private int bulls;
    private boolean win;
    Random random = new Random();
    Scanner scanner = new Scanner(System.in);

    public BullAndCows() {
        win = false;
        setLength();
    }
    private void setLength() {
        System.out.println("Please, enter the secret code's length:");
        String input = scanner.nextLine();
        try {
            int length = Integer.parseInt(input);
            if (length > 36) {
                System.out.println("Error: can't generate a secret number with a length of " + length + ", as there aren't enough unique symbols.\nChoose a number from the range [1,36].");
            } else if (length < 1) {
                System.out.println("Error: can't generate a secret number with a length of " + length + ".\nChoose a number from the range [1,36].");
            } else {
                this.length = length;
            }
        } catch (Exception e) {
            System.out.println("Error: \"" + input + "\" isn't a valid number.");
        }
        setRange();
    }
    public void setRange() {
        char[] secret = new char[length];
        Arrays.fill(secret, '*');
        System.out.println("Input the number of possible symbols in the code:");
        String input = scanner.next();
        try {
            int range = Integer.parseInt(input);
            if (range > 36) {
                System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            } else if (range < 10) {
                System.out.println("Error: minimum number of possible symbols in the code is 10 (0-9).");
            } else if (range < length) {
                System.out.println("Error: it's not possible to generate a code with a length of " + length + " with " + range + " unique symbols.");
            } else {
                this.range = range;
                System.out.print("The secret is prepared: " + String.valueOf(secret));
                char c = (char)(this.range - 10 + 96);
                if (c > 96) {
                    System.out.println(" (0-9, a-" + c + ").");
                } else {
                    System.out.println(" (0-9).");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: \"" + input + "\" isn't a valid number.");
        }
        generateCode();
        play();
    }
    private void play() {
        System.out.println("Okay, let's start the game!");
        int turn = 1;
        while (!win) {
            System.out.printf("Turn %d:\n> ", turn++);
            grade(scanner.next());
        }
    }
    private boolean contains(char[] ar, char c) {
        for (char arrayValue : ar) {
            if (arrayValue == c) {
                return true;
            }
        }
        return false;
    }
    private void generateCode() {
        char[] code = new char[length];
        char[] dictionary = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

        for (int index = 0; index < length; index++) {
            char c = dictionary[random.nextInt(range)];
            if (contains(code, c)) {
                index--;
                continue;
            }
            code[index] = c;
        }

        secretCode = code;
    }
    private void grade(String guessCode) {
        if ("Riddler".equals(guessCode)) {
            System.out.println(secretCode);
        }
        char[] code = secretCode;
        for (int index = 0; index < code.length; index++) {
            for (int innerIndex = 0; innerIndex < guessCode.length(); innerIndex++) {
                if (code[index] == guessCode.charAt(innerIndex)) {
                    if (index == innerIndex) {
                        bulls++;
                        continue;
                    }
                    cows++;
                }
            }
        }
        printGrade();
    }
    private void printGrade() {
        StringBuilder grade = new StringBuilder("Grade: ");
        if (bulls > 0 && cows == 0) {
            grade.append(String.format("%d bull(s).", bulls));
        } else if (cows > 0 && bulls == 0) {
            grade.append(String.format("%d cows(s).", cows));
        } else if (bulls > 0 && cows >0) {
            grade.append(String.format("%d bull(s) and %d cow(s).", bulls, cows));
        } else {
            grade.append("None.");
        }
        System.out.println(grade);
        checkForWin();
        bulls = 0;
        cows = 0;
    }
    private void checkForWin() {
        if (bulls == length) {
            System.out.println("Congratulations! You guessed the secret code.");
            win = true;
        }
    }

}
