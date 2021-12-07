class Rational {
    private long a, b;

    public Rational(long a, long b) {
        this.a = a;
        this.b = b;
    }

    public Rational(Rational r) {
        a = r.getNominator();
        b = r.getDenominator();
    }

    public void setFraction(long a, long b) {
        this.a = a;
        this.b = b;
        simplify();
    }

    public long getNominator() {
        return a;
    }

    public long getDenominator() {
        return b;
    }

    private void simplify() {
        int signA = 1;
        if (a < 0) {
            signA = -1;
            a *= -1;
        }
        long ca = a;
        long cb = b;

        while (cb != 0) {
            long copy = cb;
            cb = ca % cb;
            ca = copy;
        }
        a = a * signA / ca;
        b = b / ca;
    }

    public Rational add(Rational toAdd) {
        Rational result = new Rational(a, b);
        result.setFraction(
                a * toAdd.getDenominator() + b * toAdd.getNominator(),
                b * toAdd.getDenominator()
        );
        result.simplify();

        return result;
    }

    public Rational substract(Rational toSubstract) {
        Rational result = new Rational(a, b);
        result.setFraction(
                a * toSubstract.getDenominator() - b * toSubstract.getNominator(),
                b * toSubstract.getDenominator()
        );
        result.simplify();

        return result;
    }

    public Rational multiply(Rational toMultiply) {
        Rational result = new Rational(a, b);
        result.setFraction(
                a * toMultiply.getNominator(),
                b * toMultiply.getDenominator()
        );
        result.simplify();

        return result;
    }

    public Rational divide(Rational toDivide) {
        Rational result = new Rational(a, b);
        result.setFraction(
                a * toDivide.getDenominator(),
                b * toDivide.getNominator()
        );
        if (result.getDenominator() < 0)
            result.setFraction(result.getNominator() * -1, result.getDenominator() * -1);
        result.simplify();

        return result;
    }

    @Override
    public String toString() {
        return a + "/" + b;
    }
}


public class Solution {
    int[][] matrix;
    Rational[][] Q;
    Rational[][] R;
    Rational[][] N;
    int terminalStateNumber;
    int[] solution;

    public Solution(int[][] m) {
        matrix = m;
        terminalStateNumber = 0;
        boolean correctPosition = false;
        while (!correctPosition) {
            correctPosition = true;
            for (int i = 1; i < matrix.length - 1; i++) {
                if (checkIfTerminalState(matrix[i]) && !checkIfTerminalState(matrix[i + 1])) {
                    correctPosition = false;
                    int[] copy = matrix[i + 1];
                    matrix[i + 1] = matrix[i];
                    matrix[i] = copy;
                    for (int j = 0; j < matrix.length; j++) {
                        int c = matrix[j][i + 1];
                        matrix[j][i + 1] = matrix[j][i];
                        matrix[j][i] = c;
                    }
                }
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            boolean onlyColumnZeros = true;
            for (int j = 0; j < matrix.length; j++){
                if (matrix[j][i] != 0 && i != j) onlyColumnZeros = false;
            }
            if (onlyColumnZeros && !checkIfTerminalState(matrix[i]) && i != 0) {
                matrix = removeColumnAndLine(matrix, i);
                i--;
            }
        }

        boolean firstStateTerminal = false;

        for (int i = 0; i < matrix.length; i++) {
            if (checkIfTerminalState(matrix[i])) {
                matrix[i][i] = 1;
                terminalStateNumber++;
                if (i == 0) firstStateTerminal = true;
            }
        }

        if (firstStateTerminal) {
            solution = new int[terminalStateNumber + 1];
            solution[0] = 1;
            solution[terminalStateNumber] = 1;
            return;
        }

        Rational[][] ratMatrix = initRational(matrix.length, matrix.length);
        for (int i = 0; i < matrix.length; i++) {
            int lineSum = 0;
            for (int j = 0; j < matrix.length; j++)
                lineSum += matrix[i][j];
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] != 0) ratMatrix[i][j] = new Rational(matrix[i][j], lineSum);
                else ratMatrix[i][j] = new Rational(0, 1);
            }
        }
        int qLength = matrix.length - terminalStateNumber;
        Rational[][] eye = initRational(qLength, qLength);
        Rational[][] reversed = initRational(qLength, qLength);
        Q = initRational(qLength, qLength);
        for (int i = 0; i < qLength; i++) {
            System.arraycopy(ratMatrix[i], 0, Q[i], 0, qLength);
            eye[i][i] = new Rational(1, 1);
            reversed[i][i] = new Rational(1, 1);
        }
        R = initRational(qLength, matrix.length - qLength);
        for (int i = 0; i < qLength; i++) {
            System.arraycopy(ratMatrix[i], qLength, R[i], 0, matrix.length - qLength);
        }
        Rational[][] matrixToReverse = initRational(qLength, qLength);
        for (int i = 0; i < qLength; i++)
            for(int j = 0; j < qLength; j++) {
                matrixToReverse[i][j] = eye[i][j].substract(Q[i][j]);
            }

        for (int i = 0; i < qLength; i++) {
            Rational pivot = new Rational(matrixToReverse[i][i]);
            if (pivot.getNominator() == 0) {
                for (int j = i + 1; j < qLength; j++) {
                    if (matrixToReverse[j][i].getNominator() != 0) {
                        swapLine(matrixToReverse, i, j);
                        swapLine(reversed, i, j);
                    }
                }
                pivot = new Rational(matrixToReverse[i][i]);
            }
            divideLine(matrixToReverse, i, pivot);
            divideLine(reversed, i, pivot);
            for (int j = 0; j < qLength; j++) {
                if (i != j) {
                    pivot = new Rational(matrixToReverse[j][i]);
                    if (pivot.getNominator() != 0) {
                        divideLine(matrixToReverse, j, pivot);
                        divideLine(reversed, j, pivot);
                        substractLine(matrixToReverse, j, i);
                        substractLine(reversed, j, i);
                    }
                }
            }
        }

        for (int i = 0; i < qLength; i++) {
            Rational pivot = new Rational(matrixToReverse[i][i]);
            matrixToReverse[i][i] = matrixToReverse[i][i].divide(pivot);
            divideLine(reversed, i, pivot);
        }
        N = reversed;
        Rational[][] M = initRational(qLength, matrix.length - qLength);
        for (int i = 0; i < qLength; i++) {
            for (int j = 0; j < matrix.length - qLength; j++) {
                for (int k = 0; k < qLength; k++) {
                    Rational product = N[i][k].multiply(R[k][j]);
                    M[i][j] = M[i][j].add(product);
                }
            }
        }
        int SCM = 1;
        solution = new int[M[0].length + 1];
        for (int i = 0; i < M[0].length; i++) {
            SCM = calculateSCM(SCM, M[0][i].getDenominator());
        }
        for (int i = 0; i < M[0].length; i++) {
            solution[i] = (int) M[0][i].getNominator() * (SCM / (int) M[0][i].getDenominator());
        }
        solution[M[0].length] = SCM;


    }

    public static int calculateSCM(long a, long b) {
        return (int) ( a * b / calculateGCD(a, b));
    }

    public static int calculateGCD(long a, long b) {
        if (a < b) {
            long copy = b;
            b = a;
            a = copy;
        }
        while (b != 0) {
            long copy = b;
            b = a % b;
            a = copy;
        }

        return (int) a;
    }

    private Rational[][] initRational(int m, int n) {
        Rational[][] toReturn = new Rational[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++)
                toReturn[i][j] = new Rational(0, 1);
        }

        return toReturn;
    }

    private void divideLine(Rational[][] m, int idx, Rational factor) {
        for (int i = 0; i < m[idx].length; i++) {
            m[idx][i] = m[idx][i].divide(factor);
        }
    }

    private void substractLine(Rational[][] m, int line1, int line2) {
        for (int i = 0; i < m[line1].length; i++) {
            m[line1][i] = m[line1][i].substract(m[line2][i]);
        }
    }

    private void swapLine(Rational[][] m, int line1, int line2) {
        for (int i = 0; i < m[line1].length; i++) {
            Rational copy = m[line1][i];
            m[line1][i] = m[line2][i];
            m[line2][i] = copy;
        }
    }

    private boolean checkIfTerminalState(int[] line) {
        for (int i = 0; i < line.length; i++) {
            if (line[i] != 0) return false;
        }

        return true;
    }

    public int[][] removeColumnAndLine(int[][] m, int k) {
        int[][] result = new int[m.length - 1][m.length - 1];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result.length; j++) {
                if (i < k && j < k) result[i][j] = m[i][j];
                else if(i < k && j >= k) result[i][j] = m[i][j + 1];
                else if(i >= k && j < k) result[i][j] = m[i + 1][j];
                else if(i >= k && j >= k) result[i][j] = m[i + 1][j + 1];
            }
        }

        return result;
    }

    public static void print2D(Rational mat[][])
    {
        // Loop through all rows
        for (int i = 0; i < mat.length; i++) {

            // Loop through all elements of current row
            for (int j = 0; j < mat[i].length; j++)
                System.out.print(mat[i][j] + " ");

            System.out.println();
        }
        System.out.println();
    }

    public static void print2D(int mat[][])
    {
        // Loop through all rows
        for (int i = 0; i < mat.length; i++) {

            // Loop through all elements of current row
            for (int j = 0; j < mat[i].length; j++)
                System.out.print(mat[i][j] + " ");

            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[][] m = {{0, 0, 0, 0, 0, 0}, {3, 32, 7, 21, 23, 25}, {0, 0, 0, 0, 0, 0}, {8, 28, 33, 17, 10, 15}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}};
        int[] toDisplay = Solution.solution(m);
        for (int i = 0; i < toDisplay.length; i++)
            System.out.println(toDisplay[i]);
    }

    public static int[] solution(int[][] m) {
        Solution s = new Solution(m);

        return s.solution;
    }
}
