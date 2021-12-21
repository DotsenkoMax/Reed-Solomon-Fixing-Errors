package ru.msu.codes;

/**
 * Конвертация сообщений
 */
public class CharToHexRepr {

    /**
     * @param arr численное представление сообщения
     * @return буквенное представление сообщения
     */
    public static char[] toChar(int[] arr) {
        char[] ans = new char[arr.length];
        for (int i = 0; i < ans.length; ++i) {
            ans[i] = (char) arr[i];
        }
        return ans;
    }

    /**
     * @param arr буквенное представление сообщения
     * @return численное представление сообщения
     */
    public static int[] toInt(char[] arr) {
        int[] ans = new int[arr.length];
        for (int i = 0; i < ans.length; ++i) {
            ans[i] = (int) arr[i];
        }
        return ans;
    }
}
