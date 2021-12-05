package ru.msu.codes.decoder;

import ru.msu.codes.GaluaFieldAriphmetic;
import ru.msu.codes.Polynomial;

import java.util.ArrayList;

public class ErrorLocator {
    GaluaFieldAriphmetic gFLogic;

    public ErrorLocator(GaluaFieldAriphmetic gFLogic) {
        this.gFLogic = gFLogic;
    }
    // Berlekamp–Massey algo
    public Polynomial findErrorLocator(Polynomial syndrome, int nSym) {
        var locators = new Polynomial(new int[]{1}, gFLogic);
        var oldLocators = new Polynomial(new int[]{1}, gFLogic);
        for (int i = 0; i < nSym; i++) {
            int delta = syndrome.get(i);
            for (int j = 1; j <= locators.degree(); j++) {
                delta = gFLogic.sum(delta, gFLogic.multiply(locators.get(j), syndrome.get(i - j)));
            }
            oldLocators = new Polynomial(new int[]{0}, gFLogic).append(oldLocators);
            if (delta != 0) {
                if (oldLocators.degree() > locators.degree()) {
                    Polynomial newLocators = oldLocators.scale(delta);
                    oldLocators = locators.scale(gFLogic.inverse(delta));
                    locators = newLocators;
                }
                locators = locators.sumReversed(oldLocators.scale(delta));
            }
        }
        locators.deleteLeadingZeroes();
        assert locators.degree() * 2 <= nSym;
        return locators;
    }

    public ArrayList<Integer> findPositions(Polynomial errLocatorPol, int msgLen) {
        ArrayList<Integer> positions = new ArrayList<>();
        int totalErrs = errLocatorPol.degree(), errsFound = 0;
        for (int i = 0; i < msgLen; i++) {
            if (errLocatorPol.evalReverse(gFLogic.getAlphaInDeg(msgLen - i - 1)) == 0) {
                positions.add(i);
                errsFound += 1;
            }
        }
        positions.sort(Integer::compareTo);
        System.out.printf("errsFound: %s, totalErrs: %s\n", errsFound, totalErrs);
        System.out.printf("Errors have positions: %s\n", positions);
        assert errsFound == totalErrs;
        return positions;

    }
}
