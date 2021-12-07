import java.math.BigInteger;

public class Solution {
    public static void main(String args[]) {
        System.out.println(Solution.solution("15"));
    }

    public static int solution(String x) {
        BigInteger pallets = new BigInteger(x);
        int counter = 0;
        BigInteger[] bigIntegers = {new BigInteger("0"), new BigInteger("1"), new BigInteger("2"), new BigInteger("3"), new BigInteger("4")};
        while (!pallets.equals(bigIntegers[1])) {
            if (pallets.mod(bigIntegers[2]).equals(bigIntegers[0])) {
                pallets = pallets.shiftRight(1);
            } else {
                if (pallets.equals(bigIntegers[3]) || pallets.mod(bigIntegers[4]).equals(bigIntegers[1])) {
                    pallets = pallets.subtract(bigIntegers[1]);
                } else {
                    pallets = pallets.add(bigIntegers[1]);
                }
            }
            counter++;
        }

        return counter;
    }
}
