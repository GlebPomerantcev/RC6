package utility;

public class RotateUtility {

    public static int rotLeft(int val, int pas) {
        return (val << pas) | (val >>> (32 - pas));
    }

    public static int rotRight(int val, int pas) {
        return (val >>> pas) | (val << (32 - pas));
    }
}
