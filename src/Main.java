
public class Main {
    static int w = 32, r = 20;
    static int[] S;
    private static int Pw = 0xb7e15163, Qw = 0x9e3779b9; //magic constant

    public static void main(String[] args) {

    }

    private static int rotLeft(int val, int pas) {
        return (val << pas) | (val >>> (32 - pas));
    }

    private static int rotRight(int val, int pas) {
        return (val >>> pas) | (val << (32 - pas));
    }

    public int[] keySchedule(byte[] key) {
        int[] S = new int[2 * r + 4];
        S[0] = Pw;
        int c = key.length / (w / 8);
        int[] L = Converting.byteArrayToWords(key, c);
        for (int i = 1; i < (2 * r + 4); i++) {
            S[i] = S[i - 1] + Qw;
        }
        int A, B, i, j;
        A = B = i = j = 0;
        int v = 3 * Math.max(c, (2 * r + 4));
        for (int s = 0; s < v; s++) {
            A = S[i] = rotRight((S[i] + A + B), 3);
            B = L[j] = rotLeft(L[j] + A + B, A + B);
            i = (i + 1) % (2 * r + 4);
            j = (j + 1) % c;
        }
        return S;
    }
}
