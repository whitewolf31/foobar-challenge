import java.util.AbstractMap;
import java.util.Map;

public class Solution {
    public int[] pegs;
    public int[] distances;
    public int minElement;
    public int maxElement;

    public Solution(int pegs[]) {
        this.pegs = pegs;
        this.distances = new int[pegs.length - 1];
        for (int i = 1; i < pegs.length; i++) {
            distances[i - 1] = pegs[i] - pegs[i - 1];
        }
        calculateMaxValue();
        calculateMinValue();
    }

    public static void main(String[] args) {
        int[] pegs = {4, 30, 50, 62};
        int[] solution = Solution.solution(pegs);
        for (int i = 0; i < solution.length; i++) {
            System.out.println(solution[i]);
        }
    }

    public void calculateMinValue() {
        int sum = 1;
        int maxEl = sum;
        for (int i = 2; i < pegs.length; i += 2) {
            sum += distances[i - 2] - distances[i - 1];
            if (sum > maxEl) maxEl = sum;
        }

        minElement = maxEl;
    }

    public void calculateMaxValue() {
        int sum = distances[0] - 1;
        int minEl = sum;
        for (int i = 3; i < pegs.length; i += 2) {
            sum += distances[i - 1] - distances[i - 2];
            if (sum < minEl) minEl = sum;
        }

        maxElement = minEl;
    }

    public static int[] solution(int[] pegs) {
        Solution s = new Solution(pegs);
        Map.Entry<Integer, Integer> sol = s.getRatio();
        int[] toReturn = new int[2];
        toReturn[0] = sol.getKey();
        toReturn[1] = sol.getValue();

        return toReturn;
    }

    public Map.Entry<Integer, Integer> getRatio() {
        if (pegs.length % 2 == 1) {
            int sum = 0;
            for (int i = 0; i < distances.length; i++) {
                int sign = i % 2 == 0 ? 1 : -1;
                sum += 2 * distances[i] * sign;
            }

            if (sum >= minElement && sum <= maxElement)
                return new AbstractMap.SimpleEntry<Integer, Integer>(sum, 1);

            return new AbstractMap.SimpleEntry<Integer, Integer>(-1, -1);
        }

        Map.Entry<Integer, Integer> sum;
        int numeratorSum = 0;
        for (int i = 0; i < distances.length; i++) {
            int sign = i % 2 == 0 ? 1 : -1;
            numeratorSum += 2 * distances[i] * sign;
        }
        if (numeratorSum / 3 >= minElement && numeratorSum / 3 <= maxElement)
            return simplify(numeratorSum, 3);

        return new AbstractMap.SimpleEntry<Integer, Integer>(-1, -1);
    }

    private Map.Entry<Integer, Integer> simplify(int a, int b) {
        int ca = a;
        int cb = b;
        while (cb != 0) {
            int copy = cb;
            cb = ca % cb;
            ca = copy;
        }

        return new AbstractMap.SimpleEntry<Integer, Integer>(a / ca, b / ca);
    }
}
