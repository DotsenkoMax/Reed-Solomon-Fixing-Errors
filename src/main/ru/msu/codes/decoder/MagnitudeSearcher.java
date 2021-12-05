package ru.msu.codes.decoder;

import ru.msu.codes.GaluaFieldAriphmetic;
import ru.msu.codes.Polynomial;

import java.util.ArrayList;

public class MagnitudeSearcher {
    GaluaFieldAriphmetic gFLogic;

    public MagnitudeSearcher(GaluaFieldAriphmetic gFLogic) {
        this.gFLogic = gFLogic;
    }

    public Polynomial findRealPolynomial(Polynomial magnitude, Polynomial corrupted, int nSym) {
        assert magnitude.degree() == magnitude.degree();
        return magnitude.sum(corrupted).getPolWithoutSuffix(nSym);
    }

    // Forney Algorithm
    public Polynomial findMagnitude(ArrayList<Integer> errorIndices, Polynomial syndrome, Polynomial errorLocator, int nSym, int encodedMessageLen) {
        Polynomial multSyndErrors = syndrome.mult(errorLocator);
        Polynomial multNoHighSuffix = multSyndErrors.getOnlySuffix(nSym);
        Polynomial derivativeErrLocator = errorLocator.deriveFormal();
        int[] magnitude = new int[encodedMessageLen];
        for (var idx : errorIndices) {
            var reversedAlphaInPositionDegree = gFLogic.inverse(gFLogic.getAlphaInDeg(idx));
            var errValue = multNoHighSuffix.eval(reversedAlphaInPositionDegree);
            var derValue = derivativeErrLocator.eval(reversedAlphaInPositionDegree);
            var currMagnitude = gFLogic.divide(errValue, derValue);
            magnitude[idx] = currMagnitude;
        }
        return new Polynomial(magnitude, gFLogic);
    }
}
