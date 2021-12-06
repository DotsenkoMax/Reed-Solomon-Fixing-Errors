package ru.msu.codes.decoder;

import ru.msu.codes.GaluaFieldAriphmetic;
import ru.msu.codes.Polynomial;

public class SyndromesPolynomialCalculator {
    GaluaFieldAriphmetic gf;

    public SyndromesPolynomialCalculator(GaluaFieldAriphmetic gf) {
        this.gf = gf;
    }

    public Polynomial calcSyndromes(Polynomial messageIn, int nSym) {
        int[] arr = new int[nSym];
        for (int i = 1; i <= nSym; i++) {
            arr[i - 1] = messageIn.eval(gf.getAlphaInDeg(i));
        }
        return new Polynomial(arr, gf);
    }
}
