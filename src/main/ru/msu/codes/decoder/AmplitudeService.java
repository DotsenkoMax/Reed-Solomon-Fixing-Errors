package ru.msu.codes.decoder;

import ru.msu.codes.GaluaFieldAriphmetic;
import ru.msu.codes.Polynomial;

import java.util.List;

public class AmplitudeService {
    GaluaFieldAriphmetic gFLogic;

    public AmplitudeService(GaluaFieldAriphmetic gFLogic) {
        this.gFLogic = gFLogic;
    }

    public Polynomial findRealPolynomial(Polynomial magnitude, Polynomial corrupted) {
        assert magnitude.degree() == magnitude.degree();
        return magnitude.sum(corrupted);
    }

    // Forney Algorithm
    public Polynomial findMagnitude(List<Integer> errorIndices, Polynomial syndrome, Polynomial errorLocator, int nSym, int encodedMessageLen) {
        Polynomial nominator = syndrome.mult(errorLocator).getOnlyPrefix(nSym); // error polynom = (def) syndrome * errorLocator
        Polynomial derivativeErrLocator = errorLocator.deriveFormal();
        int[] magnitude = new int[encodedMessageLen];
        for (var idx : errorIndices) {
            var reversedAlphaInPositionDegree = gFLogic.inverse(gFLogic.getAlphaInDeg(idx));
            var errValue = nominator.eval(reversedAlphaInPositionDegree);
            var derValue = derivativeErrLocator.eval(reversedAlphaInPositionDegree);
            var currMagnitude = gFLogic.divide(errValue, derValue);
            magnitude[idx] = currMagnitude;
        }
        return new Polynomial(magnitude, gFLogic);
    }
}
