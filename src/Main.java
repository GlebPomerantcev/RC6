
import utility.ConvertingUtility;
import utility.RotateUtility;

import java.io.*;

public class Main {

    private static int w = 32, r = 20;
    private static int[] S;
    private static int Pw = 0xb7e15163, Qw = 0x9e3779b9; //magic constant

    public static void main(String[] args) {

        try {
            FileReader input = new FileReader("input.txt");
            FileWriter output = new FileWriter("output.txt");
            BufferedReader bf = new BufferedReader(input);

            String operationType = bf.readLine();
            String inputText = bf.readLine().replaceAll(" ", "");
            String inputKey = bf.readLine().replaceAll(" ", "");

            byte[] text = ConvertingUtility.hexToByteArray(inputText);
            byte[] key = ConvertingUtility.hexToByteArray(inputKey);

            S = keySchedule(key);

            switch (operationType) {
                case "encryption": {
                    byte[] resArr = encryption(text);
                    String resString = ConvertingUtility.byteArrayToHex(resArr).replaceAll("..", "$0 ");
                    output.write("result is: " + resString);
                    output.flush();
                    break;
                }
                case "decryption": {
                    byte[] resArr = decryption(text);
                    String resString = ConvertingUtility.byteArrayToHex(resArr).replaceAll("..", "$0 ");
                    output.write("result is: " + resString);
                    output.flush();
                    break;
                }
                default:
                    System.out.println("Wrong operation type");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] encryption(byte[] key) {
        int temp, t, u, lgw = 5;
        byte[] res;
        int[] data = new int[key.length / 4];
        for (int i = 0; i < data.length; i++)
            data[i] = 0;
        data = ConvertingUtility.byteArrayToInt(key, data.length);

        int A = data[0], B = data[1], C = data[2], D = data[3];

        B += S[0];
        D += S[1];

        for (int i = 1; i <= r; i++) {
            t = RotateUtility.rotLeft(B * (2 * B + 1), lgw);
            u = RotateUtility.rotLeft(D * (2 * D + 1), lgw);
            A = RotateUtility.rotLeft(A ^ t, u) + S[2 * i];
            C = RotateUtility.rotLeft(C ^ u, t) + S[2 * i + 1];

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

        res = ConvertingUtility.intArrayToByte(data, key.length);
        return res;
    }

    private static byte[] decryption(byte[] input) {
        int temp, t, u, lgw = 5;
        int[] data = new int[input.length / 4];
        for (int i = 0; i < data.length; i++)
            data[i] = 0;
        data = ConvertingUtility.byteArrayToInt(input, data.length);

        int A = data[0], B = data[1], C = data[2], D = data[3];

        C = C - S[2 * r + 3];
        A = A - S[2 * r + 2];

        byte[] res;
        for (int i = r; i >= 1; i--) {
            temp = D;
            D = C;
            C = B;
            B = A;
            A = temp;

            u = RotateUtility.rotLeft(D * (2 * D + 1), lgw);
            t = RotateUtility.rotLeft(B * (2 * B + 1), lgw);
            C = RotateUtility.rotRight(C - S[2 * i + 1], t) ^ u;
            A = RotateUtility.rotRight(A - S[2 * i], u) ^ t;

        }
        D = D - S[1];
        B = B - S[0];

        data[0] = A;
        data[1] = B;
        data[2] = C;
        data[3] = D;

        res = ConvertingUtility.intArrayToByte(data, input.length);
        return res;
    }

    private static int[] keySchedule(byte[] key) {
        int[] S = new int[2 * r + 4];
        S[0] = Pw;
        int c = key.length / (w / 8);
        int[] L = ConvertingUtility.byteArrayToWords(key, c);

        for (int i = 1; i < (2 * r + 4); i++) {
            S[i] = S[i - 1] + Qw;
        }

        int A, B, i, j;
        int v = 3 * Math.max(c, (2 * r + 4));
        A = B = i = j = 0;

        for (int s = 0; s < v; s++) {
            A = S[i] = RotateUtility.rotLeft((S[i] + A + B), 3);
            B = L[j] = RotateUtility.rotLeft(L[j] + A + B, A + B);
            i = (i + 1) % (2 * r + 4);
            j = (j + 1) % c;
        }
        return S;
    }
}
