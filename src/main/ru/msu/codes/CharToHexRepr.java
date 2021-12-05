package ru.msu.codes;

import java.util.Arrays;
import java.util.List;

public class CharToHexRepr {
    public static List<Short> toHex(char[] arr) {
        Short[] ans = new Short[arr.length];
        for (int i = 0; i < ans.length; ++i) {
            ans[i] = (short) arr[i];
        }
        return Arrays.asList(ans);
    }

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
