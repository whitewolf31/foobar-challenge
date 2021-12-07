import java.util.ArrayList;
import java.util.Arrays;

public class Solution {
    private String toEncode;
    private final int offset = 97;

    public final ArrayList<Integer> brailleBase = new ArrayList<Integer>(
        Arrays.asList(0x20, 0x30, 0x24, 0x26, 0x22, 0x34, 0x36, 0x32, 0x14, 0x16) //first 10 characters in hex
    );

    public Solution(String toEncode) {
        this.toEncode = toEncode;
    }

    public static void main (String[] args) {
        Solution.solution("The quick brown fox jumps over the lazy dog");
    }

    public static void solution(String string) {
        Solution solution = new Solution(string);
        System.out.println(solution.encode());
    }

    public String encode() {
        StringBuffer solution = new StringBuffer();
        for (int i = 0; i < toEncode.length(); i++) {
            int currentAscii = toEncode.charAt(i);
            int numberToEncode = 0;
            if (currentAscii == 32) numberToEncode = 0x0; // space
            else if (currentAscii < 97) {
                solution.append(toBinaryString(0x1)); // Capitalize
                currentAscii += 32;
            }

            if (currentAscii == 119) numberToEncode = 0x17; // w in braille
            else if (currentAscii >= 97) {
                if (currentAscii > 119) currentAscii--; // Take into consideration the w
                int withoutAsciiOffset = currentAscii - offset;
                int brailleRow = Math.floorDiv(withoutAsciiOffset, 10);
                int brailleOffset = withoutAsciiOffset % 10;
                int toAdd;
                if (brailleRow == 0) toAdd = 0;
                else if (brailleRow == 1) toAdd = 8;
                else toAdd = 9;
                numberToEncode = brailleBase.get(brailleOffset) + toAdd;
            }
            solution.append(toBinaryString(numberToEncode));
        }

        return solution.toString();
    }

    public String toBinaryString(int number) {
        StringBuffer s = new StringBuffer();
        for (int i = 5; i >= 0; i--) {
            if ((number & (1 << i)) != 0) s.append(1);
            else s.append(0);
        }

        return s.toString();
    }
}
