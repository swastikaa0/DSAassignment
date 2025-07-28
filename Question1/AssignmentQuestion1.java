package Question1;
import java.util.*;

public class AssignmentQuestion1{

    public static int maximizeCapital(int k, int c, int[] revenues, int[] investments) {
        int n = revenues.length;

        // List of all projects (investment, revenue)
        List<int[]> projects = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            projects.add(new int[]{investments[i], revenues[i]});
        }

        // Sort projects by required investment (ascending)
        projects.sort(Comparator.comparingInt(a -> a[0]));

        // Max-heap to select the project with max revenue we can afford
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

        int i = 0;
        for (int j = 0; j < k; j++) {
            // Add all affordable projects to the max-heap
            while (i < n && projects.get(i)[0] <= c) {
                maxHeap.offer(projects.get(i)[1]); // add revenue to max-heap
                i++;
            }

            // No more projects we can afford
            if (maxHeap.isEmpty()) {
                break;
            }

            // Take the most profitable project
            c += maxHeap.poll();
        }

        return c;
    }

    // Example usage
    public static void main(String[] args) {
        int[] revenues1 = {2, 5, 8};
        int[] investments1 = {0, 2, 3};
        System.out.println(maximizeCapital(2, 0, revenues1, investments1)); // Output: 7

        int[] revenues2 = {3, 6, 10};
        int[] investments2 = {1, 3, 5};
        System.out.println(maximizeCapital(3, 1, revenues2, investments2)); // Output: 19
    }
}