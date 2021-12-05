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
        for (int i = nSym - 1; i >= 0; i--) {
            arr[i] = messageIn.eval(gf.getAlphaInDeg(nSym - 1 - i));
        }
        return new Polynomial(arr, gf);
    }
}
