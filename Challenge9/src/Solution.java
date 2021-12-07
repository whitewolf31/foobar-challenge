import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class Solution {
    public static BigInteger[] bigInts = {new BigInteger("0"), new BigInteger("1"), new BigInteger("2")};
    public static void main(String args[]) {
        //System.out.println(solution("123456789098765432123456789098765432123456"));
        System.out.println(solution("77"));
    }

    public static String solution(String n) {
        BigInteger N = new BigInteger(n);
        return beatty(N).toString();
    }

    public static BigInteger beatty(BigInteger N) {
        if (N.equals(bigInts[0])) return bigInts[0];
        BigDecimal s = new BigDecimal("2").sqrt(new MathContext(1000));
        BigInteger J = s.subtract(new BigDecimal("1")).multiply(new BigDecimal(N)).toBigInteger();
        BigInteger res = N.multiply(J).add(N.multiply(N.add(bigInts[1])).divide(bigInts[2])).subtract(J.multiply(J.add(bigInts[1])).divide(bigInts[2]));

        return res.subtract(beatty(J));
    }

//    public static BigInteger beatty2(BigInteger N) {
//        if (N.equals(bigInts[0])) return bigInts[0];
//        BigDecimal s = BigDecimal.valueOf(Math.sqrt(2));
//        BigInteger J = s.subtract(BigDecimal.valueOf(1)).multiply(new BigDecimal(N)).toBigIntegerExact();
//        BigInteger res = N.multiply(J).add(N.multiply(N.add(bigInts[1])).divide(bigInts[2])).subtract(J.multiply(J.add(bigInts[1])).divide(bigInts[2]));
//
//        return res.subtract(beatty(J));
//    }
//    public static BigInteger beatty(BigInteger N, BigDecimal s) {
//        if (N.equals(bigInts[0])) return bigInts[0];
//        BigDecimal x = s.subtract(BigDecimal.valueOf(1));
//        BigInteger J =
//
//        return res.subtract(beatty(J));
//    }

}
