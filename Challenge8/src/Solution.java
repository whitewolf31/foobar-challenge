import java.util.ArrayList;
import java.util.Comparator;

class State {
    private Integer predecessor;
    private Integer cost;
    public State(int cost, int pred) {
        this.cost = cost;
        predecessor = pred;
    }

    public void setPredecessor(int pred) {
        predecessor = pred;
    }

    public int getPredecessor() {
        return predecessor;
    }

    public void changeCost(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return cost + " [" + predecessor + "]";
    }
}

public class Solution {
    public static ArrayList<ArrayList<Integer>> permutatedLists;
    public static boolean negativeLoop = false;

    public static void main(String args[]) {
        //int[][] test = {{0, 2, 2, 2, 2, 2, -1}, {9, 0, 2, 2, 3, -1, -1}, {9, 3, 0, 2, 2, 2, -1}, {9, 3, 2, 0, 2, 2, -1}, {9, 3, 2, 2, 0, 2, -1}, {9, 3, 2, 2, 2, 0, -1}, {9, 3, 2, 2, 3, 3, 0}};
        int[][] test = {{0, 2, 2, 2, -1}, {9, 0, 2, 2, -1}, {9, 3, 0, 2, -1}, {9, 3, 2, 0, -1}, {9, 3, 2, 2, 0}};
        int[] solution = Solution.solution(test, 1);
        for (int i = 0; i < solution.length; i++)
            System.out.println(solution[i]);
    }

    public static int[] solution(int[][] times, int time_limit) {
        ArrayList<Integer> arraySolution = new ArrayList<Integer>();
        State[][] allDistances = new State[times.length][times.length];
        for (int i = 0; i < allDistances.length; i++) {
            allDistances[i] = bellmanFordman(i, times);
            if (Solution.negativeLoop) {
                int[] returnValues = new int[allDistances.length - 2];
                for (int j = 0; j < allDistances.length - 2; j++) {
                    returnValues[j] = j;
                }

                return returnValues;
            }
        }
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < allDistances.length; i++) {
            for (int j = 0; j < allDistances.length; j++) {
                s.append(allDistances[i][j] + " ");
            }
            s.append("\n");
        }
        System.out.println(s);
        int bunnies = allDistances.length - 2;
        ArrayList<Integer> combinationList = new ArrayList();
        for (int i = 0; i < (1 << bunnies); i++) {
            combinationList.add(i);
        }
        combinationList.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int c1, c2;
                c1 = c2 = 0;
                int copy1, copy2;
                copy1 = o1;
                copy2 = o2;
                while (copy1 > 0 || copy2 > 0) {
                    c1 += (copy1 % 2);
                    c2 += (copy2 % 2);
                    copy1 /= 2;
                    copy2 /= 2;
                }
                if (c1 == c2) return o2 - o1;

                return c2 - c1;
            }
        });
        for (int current: combinationList) {
            ArrayList<Integer> currentBunnies = new ArrayList();
            int idx = 1;
            for (int i = (1 << (bunnies - 1)); i > 0; i >>= 1) {
                if ((i & current) > 0) currentBunnies.add(idx);
                idx++;
            }
            Solution.permutatedLists = new ArrayList();
            permutateSolutions(currentBunnies, 0);
            int minCost = Integer.MAX_VALUE;
            for (ArrayList<Integer> list: Solution.permutatedLists) {
                int currentCost = 0;
                State[] currentState = allDistances[0];
                int currentIdx = 0;
                ArrayList<Integer> visitedNodes = new ArrayList<Integer>();
                for (int currentBunny: list) {
                    if (visitedNodes.contains(currentBunny)) continue;
                    currentCost += currentState[currentBunny].getCost();
                    int currentPred = currentState[currentBunny].getPredecessor();
                    while (currentPred != currentIdx) {
                        if (!visitedNodes.contains(currentPred)) visitedNodes.add(currentPred);
                        currentPred = currentState[currentPred].getPredecessor();
                    }
                    currentState = allDistances[currentBunny];
                    currentIdx = currentBunny;
                }
                currentCost += currentState[allDistances.length - 1].getCost();
                if (currentCost < minCost) minCost = currentCost;
            }
            if (minCost <= time_limit) {
                arraySolution = currentBunnies;
                break;
            };
        }
        int[] sol = arraySolution.stream().mapToInt(i -> i - 1).toArray();
        return sol;
    }

    public static void permutateSolutions(ArrayList<Integer> permutable, int k) {
        if (k == permutable.size()) {
            if (!permutatedLists.contains(permutable))
                permutatedLists.add(permutable);
        }
        for (int i = k; i < permutable.size(); i++) {
            ArrayList<Integer> clone = (ArrayList<Integer>) permutable.clone();
            int temp = clone.get(i);
            clone.set(i, clone.get(k));
            clone.set(k, temp);
            permutateSolutions(clone, k + 1);
            temp = clone.get(i);
            clone.set(i, clone.get(k));
            clone.set(k, temp);
        }
    }

    public static State[] bellmanFordman(int head, int[][] graph) {
        State[] distances = new State[graph.length];
        for (int i = 0; i < graph.length; i++)
            distances[i] = new State(Integer.MAX_VALUE, head);
        distances[head].changeCost(0);
        boolean changed = true;
        for (int k = 0; k < graph.length; k++) {
            changed = false;
            for (int i = 0; i < graph.length; i++) {
                for (int j = 0; j < graph.length; j++) {
                    if (i != j && distances[i].getCost() != Integer.MAX_VALUE && distances[i].getCost() + graph[i][j] < distances[j].getCost()) {
                        distances[j].changeCost(distances[i].getCost() + graph[i][j]);
                        distances[j].setPredecessor(i);
                        changed = true;
                    }
                }
            }
        }
        if (changed) {
            Solution.negativeLoop = true;
            State[] returnValues = new State[graph.length];
            for (int i = 0; i < graph.length; i++) {
                returnValues[i] = new State(0, head);
            }
            return returnValues;
        }

        return distances;
    }


}
