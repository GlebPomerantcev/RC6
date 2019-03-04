class Converting {

    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if (digit == -1) {
            throw new IllegalArgumentException("Invalid Hexadecimal Character: " + hexChar);
        }
        return digit;
    }

    private static byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    static String byteArrayToHex(byte[] byteArray) {
        StringBuilder hexStringBuffer = new StringBuilder();
        for (byte aByteArray : byteArray) {
            hexStringBuffer.append(byteToHex(aByteArray));
        }
        return hexStringBuffer.toString();
    }

    static byte[] hexToByteArray(String hexString) {
        if (hexString.length() % 2 == 1) {
            throw new IllegalArgumentException("Invalid hexadecimal String supplied.");
        }
        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
        }
        return bytes;
    }

    static byte[] intArrayToByte(int[] intArray, int length) {
        byte[] res = new byte[length];
        for (int i = 0; i < length; i++) {
            res[i] = (byte) ((intArray[i / 4] >>> (i % 4) * 8) & 0xff);
        }
        return res;
    }

    static int[] byteArrayToInt(byte[] byteArray, int length) {
        int[] res = new int[length];
        for (int j = 0; j < res.length; j++) {
            res[j] = 0;
        }
        int counter = 0;
        for (int i = 0; i < res.length; i++) {
            res[i] = ((byteArray[counter++] & 0xff)) |
                    ((byteArray[counter++] & 0xff) << 8) |
                    ((byteArray[counter++] & 0xff) << 16) |
                    ((byteArray[counter++] & 0xff) << 24);
        }
        return res;
    }

    static int[] byteArrayToWords(byte[] key, int c) {
        int[] tmp = new int[c];
        for (int i = 0; i < tmp.length; i++)
            tmp[i] = 0;
        for (int i = 0, off = 0; i < c; i++)
            tmp[i] = ((key[off++] & 0xFF)) | ((key[off++] & 0xFF) << 8)
                    | ((key[off++] & 0xFF) << 16) | ((key[off++] & 0xFF) << 24);
        return tmp;
    }
}
