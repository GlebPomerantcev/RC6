
public class Main {
    private static int w = 32, r = 20;
    private static int[] S;
    private static int Pw = 0xb7e15163, Qw = 0x9e3779b9; //magic constant

    public static void main(String[] args) {

    }

    private static int rotLeft(int val, int pas) {
        return (val << pas) | (val >>> (32 - pas));
    }

    private static int rotRight(int val, int pas) {
        return (val >>> pas) | (val << (32 - pas));
    }

    public static int[] keySchedule(byte[] key) {
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

    public byte[] encryption(byte[] key) {
        int temp, t, u;
        int[] data = new int[key.length / 4];
        for (int i = 0; i < data.length; i++)
            data[i] = 0;

        data = Converting.byteArrayToInt(key, data.length);

        int A = data[0], B = data[1], C = data[2], D = data[3];

        B += S[0];
        D += S[1];

        int lgw = 5;

        byte[] res;
        for (int i = 1; i <= r; i++) {

            t = rotLeft(B * (2 * B + 1), lgw);
            u = rotLeft(D * (2 * D + 1), lgw);
            A = rotRight(A ^ t, u) + S[2 * i];
            C = rotRight(C ^ u, t) + S[2 * i + 1];

            temp = A;
            A = B;
            B = C;
            C = D;
            D = temp;
        }

        A += S[2 * r + 2];
        C += S[2 * r + 3];

        data[0] = A;
        data[1] = B;
        data[2] = C;
        data[3] = D;

        res = Converting.intArrayToByte(data, key.length);
        return res;
    }

}
