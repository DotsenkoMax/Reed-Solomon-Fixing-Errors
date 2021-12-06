package ru.msu.codes;


public class CharToHexRepr {

    public static char[] toChar(int[] arr) {
        char[] ans = new char[arr.length];
        for (int i = 0; i < ans.length; ++i) {
            ans[i] = (char) arr[i];
        }
        return ans;
    }

    public static int[] toInt(char[] arr) {
        int[] ans = new int[arr.length];
        for (int i = 0; i < ans.length; ++i) {
            ans[i] = (int) arr[i];
        }
        return ans;
    }
}
