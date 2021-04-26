package bullscows;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
public class Main {
    public static void main(String[] args) {
        new BullAndCows();
    }
}
class BullAndCows {
    private int length;
    private int charLength;
    private char[] secretCode;
    private int cows;
    private int bulls;
    private boolean win;
    Random random = new Random();
    Scanner scanner = new Scanner(System.in);
    public void play() {
        System.out.println("Okay, let's start the game!");
        int turn = 1;
        while (!win) {
            System.out.printf("Turn %d:\n> ", turn++);
            String guess = scanner.next();
            grade(guess);
        }
    }
    BullAndCows() {
        win = false;
        setLength();
    }
    void setLength() {
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
            setCharLength();
        } catch (Exception e) {
            System.out.println("Error: \"" + input + "\" isn't a valid number.");
        }
    }
    private char getCharFromInt(int number) {
        switch(number) {
            case 0: return '0';
            case 1: return '1';
            case 2: return '2';
            case 3: return '3';
            case 4: return '4';
            case 5: return '5';
            case 6: return '6';
            case 7: return '7';
            case 8: return '8';
            case 9: return '9';
        }
        return 'x';
    }
    private boolean contains(char[] ar, char c) {
        for (char arrayValue : ar) {
            if (arrayValue == c) {
                return true;
            }
        }
        return false;
    }
    private boolean contains(int[] ar, int c) {
        for (int arrayValue : ar) {
            if (arrayValue == c) {
                return true;
            }
        }
        return false;
    }
    private void ifCharLength0(char[] code) {
        for (int index = 0; index < length; index++) {
            char n = getCharFromInt(random.nextInt(10));
            if (contains(code, n)) {
                index--;
                continue;
            }
            code[index] = n;
        }
    }
    private void setValues(char[] code, String TYPE, int[] indexes) {
        char c;
        for (int i = 0; i < indexes.length; i++) {
            if("Char".equals(TYPE)) {
                c = (char) (random.nextInt(charLength) + 'a');
            } else {
                c = getCharFromInt(random.nextInt(10));
            }
            if (contains(code, c)) {
                i--;
                continue;
            }
            code[indexes[i] - 1] = c;
        }
    }
    public void generateCode() {
        char[] code = new char[length];
        Random random = new Random();
        int charPlaces;
        int intPlaces;
        int[] intIndexes;
        int[] charIndexes;

        if (length <= 10) {
            if (charLength == 0) {
                ifCharLength0(code);
                secretCode = code;
                return;
            } else if (charLength < 10) {
                charPlaces = random.nextInt(Math.min(charLength, length)) + 1;
                intPlaces = length - charPlaces;
            } else {
                intPlaces = random.nextInt(length) + 1;
                charPlaces = length - intPlaces;
            }
        } else {
            charPlaces = ThreadLocalRandom.current().nextInt(length - 10, (Math.min(charLength, length)) + 1);
            intPlaces = length - charPlaces;
        }

        if (charPlaces != 0) {
            charIndexes = new int[charPlaces];
            for (int i = 0; i < charPlaces; i++ ) {
                int index = random.nextInt(length) + 1;
                if (contains(charIndexes, index)) {
                    i--;
                    continue;
                }
                charIndexes[i] = index;
            }
            setValues(code, "Char", charIndexes);
        } else {
            ifCharLength0(code);
            secretCode = code;
            return;
        }
        if (intPlaces != 0) {
            intIndexes = new int[intPlaces];
            for (int i = 0, index = 1; i < intPlaces; i++, index++) {
                if (contains(charIndexes, index)) {
                    i--;
                    continue;
                }
                intIndexes[i] = index;
            }
            setValues(code, "Int", intIndexes);
        }

        secretCode = code;
    }
    public void grade(String guessCode) {
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
    public void setCharLength() {
        char[] secret = new char[length];
        Arrays.fill(secret, '*');
        System.out.println("Input the number of possible symbols in the code:");
        String input = scanner.next();
        try {
            int charLength = Integer.parseInt(input);
            if (charLength > 36) {
                System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            } else if (charLength < 10) {
                System.out.println("Error: minimum number of possible symbols in the code is 10 (0-9).");
            } else if (charLength < length) {
                System.out.println("Error: it's not possible to generate a code with a length of " + length + " with " + charLength + " unique symbols.");
            } else {
                this.charLength = charLength - 10;
                generateCode();
                System.out.print("The secret is prepared: " + String.valueOf(secret));
                char c = (char)(this.charLength + 96);
                if (c > 96) {
                    System.out.println(" (0-9, a-" + c + ").");
                } else {
                    System.out.println(" (0-9).");
                }
                play();
            }
        } catch (Exception e) {
            System.out.println("Error: \"" + input + "\" isn't a valid number.");
        }
    }

}
