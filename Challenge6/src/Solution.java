import java.math.BigInteger;

public class Solution {
    public static void main(String args[]) {
        System.out.println(Solution.solution("224123122341231234124132451280812", "43214214281231242574293045124762"));
    }

    public static String solution(String x, String y) {
        BigInteger M = new BigInteger(x);
        BigInteger F = new BigInteger(y);
        BigInteger operations = new BigInteger("0");
        BigInteger[] bigIntegers = { new BigInteger("0"), new BigInteger("1") };
        while (!F.equals(bigIntegers[0])) {
            operations = operations.add(M.divide(F));
            BigInteger copy = F;
            F = M.mod(F);
            M = copy;
        }
        if (M.equals(bigIntegers[1])) {
            operations = operations.subtract(bigIntegers[1]);

            return operations.toString();
        }
        return "impossible";
    }
}
