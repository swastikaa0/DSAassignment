package Question3;
public class AssignmentQuestion3a {

    public static int maxRepetitions(String p1, int t1, String p2, int t2) {
        int len1 = p1.length();
        int len2 = p2.length();

        int i = 0, j = 0;  // pointers in p1 and p2
        int count1 = 0;    // how many times p1 repeated processed
        int count2 = 0;    // how many times p2 repeated matched

        while (count1 < t1) {
            if (p1.charAt(i) == p2.charAt(j)) {
                j++;
                if (j == len2) {
                    j = 0;
                    count2++;  // matched one full p2 sequence
                }
            }
            i++;
            if (i == len1) {
                i = 0;
                count1++;  // finished one repeat of p1
            }
        }

        int result = Math.min(count2, t2);
        System.out.println("Matched repetitions: " + count2 + ", Requested: " + t2 + ", Returning: " + result);
        return result;
    }

    public static void main(String[] args) {
        String p1 = "bca";
        int t1 = 6;
        String p2 = "ba";

        int t2_1 = 3;
        int result1 = maxRepetitions(p1, t1, p2, t2_1);
        System.out.println("Result for t2 = " + t2_1 + ": " + result1);  // Expected: 3

        int t2_2 = 5;
        int result2 = maxRepetitions(p1, t1, p2, t2_2);
        System.out.println("Result for t2 = " + t2_2 + ": " + result2);  // Expected: 3
    }
}


