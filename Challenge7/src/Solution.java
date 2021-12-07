import java.util.ArrayList;

public class Solution {
    private ArrayList<ArrayList<Integer>> matrix;
    private Integer n, k;
    private Integer step;

    public Solution(int n, int k) {
        this.n = n;
        this.k = k;
        step = 0;
        matrix = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < n; i++)
            matrix.add(new ArrayList<Integer>());
    }

    private void addToMatrix(int combination) {
        int idx = 0;
        while (combination > 0) {
            if (combination % 2 == 1) matrix.get(idx).add(step);
            combination >>= 1;
            idx++;
        }
        step++;
    }

    public void createMatrix(int current, int n, int k, int position) {
        if (k == 0) {
            addToMatrix(current);
            return;
        }
        if (position == n) return;

        int bit = 1 << position;
        createMatrix(current | bit, n, k - 1, position + 1);
        createMatrix(current, n, k, position + 1);

    }

    public static void main(String args[]) {
        int[][] res = Solution.solution(3, 2);
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[i].length; j++) {
                s.append(res[i][j] + " ");
            }
            s.append("\n");
        }
        System.out.println(s);
    }

    public static int[][] solution(int n, int k) {
        if (k == 0) {
            int[][] res = new int[n][];
            for (int i = 0; i < n; i++) {
                res[i] = new int[0];
            }

            return res;
        }
        Solution s = new Solution(n, k);
        s.createMatrix(0, n, n - k + 1, 0);
        int[][] res = new int[n][];
        for (int i = 0; i < n; i++) {
            res[i] = s.matrix.get(i).stream().mapToInt(j -> j).toArray();
        }

        return res;
    }
}
