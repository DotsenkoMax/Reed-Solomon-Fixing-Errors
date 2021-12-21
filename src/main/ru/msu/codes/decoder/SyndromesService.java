package ru.msu.codes.decoder;

import ru.msu.codes.GaluaFieldArithmetic;
import ru.msu.codes.Polynomial;

/**
 * Генератор полинома синдромов
 */
public class SyndromesService {
    /**
     * Арифметика поля Галуа
     */
    GaluaFieldArithmetic gf;

    public SyndromesService(GaluaFieldArithmetic gf) {
        this.gf = gf;
    }

    /**
     * @param messageIn полученное закодированное сообщение
     * @param nSym      количсетво избыточных символов
     * @return полином синдромов
     */
    public Polynomial calcSyndromes(Polynomial messageIn, int nSym) {
        int[] arr = new int[nSym];
        for (int i = 1; i <= nSym; i++) {
            arr[i - 1] = messageIn.eval(gf.getAlphaInDeg(i));
        }
        return new Polynomial(arr, gf);
    }
}
