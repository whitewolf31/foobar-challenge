import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class Solution {
    public ArrayList<Integer> plates;
    private String code;
    private Integer mask;

    public static void main(String[] args) {
        int[] test = {7, 1};
        System.out.println(Solution.solution(test));
    }

    public Solution(int[] l) {
        plates = new ArrayList<Integer>();
        for (int plate: l) {
            plates.add(plate);
        }
        Collections.sort(plates, Collections.reverseOrder());
        code = "";
        mask = (1 << plates.size()) - 1; // size of plates bits activated
    }

    public static int solution(int[] l) {
        Solution s = new Solution(l);
        int correctMask = s.getMask();
        if (correctMask != 0) s.writeCode(correctMask);
        else return 0;
        return Integer.parseInt(s.getCode());
    }

    public boolean isDivisibleBy3(int currentMask) {
        int sum = 0;
        for (int i = 0; i < plates.size(); i++) {
            int andMask = 1 << i;
            if ((andMask & currentMask) != 0)
                sum += plates.get(plates.size() - i - 1);
        }

        if (sum % 3 == 0) return true;

        return false;
    }

    public int getMask() {
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.add(mask);
        while (queue.size() > 0) {
            int currentMask = queue.remove();
            if (isDivisibleBy3(currentMask)) return currentMask;
            for (int i = 0; i < plates.size(); i++) {
                int changeMask = 1 << i;
                if ((changeMask & currentMask) != 0)
                    queue.add(changeMask ^ currentMask);
            }
        }

        return 0;
    }

    public void writeCode(int goodMask) {
        for (int i = plates.size() - 1; i >= 0; i--) {
            int andMask = 1 << i;
            if ((andMask & goodMask) != 0)
                code = code + plates.get(plates.size() - i - 1);
        }
    }

    public String getCode() {
        return code;
    }
}

