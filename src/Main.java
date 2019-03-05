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
            switch (operationType) {
                case "encryption": {
                    String inputText = bf.readLine().replaceAll(" ", "");
                    String inputKey = bf.readLine().replaceAll(" ", "");
                    byte[] W = Converting.hexToByteArray(inputText);
                    byte[] key = Converting.hexToByteArray(inputKey);
                    S = keySchedule(key);

                    byte[] resArr = encryption(W);
                    String resString = Converting.byteArrayToHex(resArr).replaceAll("..", "$0 ");
                    output.write("result is: " + resString);
                    output.flush();
                    break;
                }
                case "decryption": {
                    String inputText = bf.readLine().replaceAll(" ", "");
                    String inputKey = bf.readLine().replaceAll(" ", "");
                    byte[] X = Converting.hexToByteArray(inputText);
                    byte[] key = Converting.hexToByteArray(inputKey);
                    S = keySchedule(key);

                    byte[] resArr = decryption(X);
                    String resString = Converting.byteArrayToHex(resArr).replaceAll("..", "$0 ");
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

    private static int rotLeft(int val, int pas) {
        return (val << pas) | (val >>> (32 - pas));
    }

    private static int rotRight(int val, int pas) {
        return (val >>> pas) | (val << (32 - pas));
    }

    private static byte[] encryption(byte[] key) {
        int temp, t, u, lgw = 5;
        byte[] res;
        int[] data = new int[key.length / 4];
        for (int i = 0; i < data.length; i++)
            data[i] = 0;
        data = Converting.byteArrayToInt(key, data.length);

        int A = data[0], B = data[1], C = data[2], D = data[3];

        B += S[0];
        D += S[1];

        for (int i = 1; i <= r; i++) {
            t = rotLeft(B * (2 * B + 1), lgw);
            u = rotLeft(D * (2 * D + 1), lgw);
            A = rotLeft(A ^ t, u) + S[2 * i];
            C = rotLeft(C ^ u, t) + S[2 * i + 1];

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

    private static byte[] decryption(byte[] input) {
        int temp, t, u, lgw = 5;
        int[] data = new int[input.length / 4];
        for (int i = 0; i < data.length; i++)
            data[i] = 0;
        data = Converting.byteArrayToInt(input, data.length);

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

            u = rotLeft(D * (2 * D + 1), lgw);
            t = rotLeft(B * (2 * B + 1), lgw);
            C = rotRight(C - S[2 * i + 1], t) ^ u;
            A = rotRight(A - S[2 * i], u) ^ t;

        }
        D = D - S[1];
        B = B - S[0];

        data[0] = A;
        data[1] = B;
        data[2] = C;
        data[3] = D;

        res = Converting.intArrayToByte(data, input.length);
        return res;
    }
