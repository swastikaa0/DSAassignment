package Question2;
import java.util.*;

public class AssignmentQuestion2b{

    static String[] words = {"STAR", "MOON"};
    static String result = "NIGHT";

    static Map<Character, Integer> charToDigit = new HashMap<>();
    static boolean[] usedDigits = new boolean[10];
    static List<Character> letters = new ArrayList<>();

    public static void main(String[] args) {
        // Collect unique letters
        Set<Character> uniqueLetters = new HashSet<>();
        for (String w : words) {
            for (char c : w.toCharArray()) uniqueLetters.add(c);
        }
        for (char c : result.toCharArray()) uniqueLetters.add(c);

        letters.addAll(uniqueLetters);

        if (letters.size() > 10) {
            System.out.println("Too many unique letters (>10), no solution possible.");
            return;
        }

        if (solve(0)) {
            printSolution();
        } else {
            System.out.println("No solution found.");
        }
    }

    // Backtracking function
    static boolean solve(int index) {
        if (index == letters.size()) {
            return checkSolution();
        }

        char c = letters.get(index);
        for (int d = 0; d <= 9; d++) {
            if (!usedDigits[d]) {
                // Leading letter cannot be zero
                if (d == 0 && isLeadingLetter(c)) continue;

                usedDigits[d] = true;
                charToDigit.put(c, d);

                if (solve(index + 1)) return true;

                // Backtrack
                usedDigits[d] = false;
                charToDigit.remove(c);
            }
        }
        return false;
    }

    static boolean isLeadingLetter(char c) {
        // Leading letters are first letters of any word or result
        for (String w : words) {
            if (w.charAt(0) == c) return true;
        }
        if (result.charAt(0) == c) return true;
        return false;
    }

    static int wordToNumber(String w) {
        int num = 0;
        for (char c : w.toCharArray()) {
            num = num * 10 + charToDigit.get(c);
        }
        return num;
    }

    static boolean checkSolution() {
        int sum = 0;
        for (String w : words) {
            sum += wordToNumber(w);
        }
        return sum == wordToNumber(result);
    }

    static void printSolution() {
        System.out.println("Solution found:");
        for (Map.Entry<Character, Integer> entry : charToDigit.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
        System.out.println();

        for (String w : words) {
            System.out.println(w + " = " + wordToNumber(w));
        }
        System.out.println(result + " = " + wordToNumber(result));
        System.out.println("Check: sum of words = " + Arrays.stream(words).mapToInt(AssignmentQuestion2b::wordToNumber).sum());
    }
}
