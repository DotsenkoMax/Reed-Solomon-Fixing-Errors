package ru.msu.codes.decoder;

import ru.msu.codes.CharToHexRepr;
import ru.msu.codes.GaluaFieldAriphmetic;
import ru.msu.codes.Polynomial;
import ru.msu.codes.PolynomialArithmetic;

public class ReedSolomonDecoderImpl implements ReedSolomonDecoder {
    public final GaluaFieldAriphmetic gFLogic;
    public final SyndromesPolynomialCalculator sundromes;
    public final ErrorLocator errorLocator;
    public final MagnitudeSearcher magnitudeSearcher;

    public ReedSolomonDecoderImpl(GaluaFieldAriphmetic gFLogic) {
        this.gFLogic = gFLogic;
        this.sundromes = new SyndromesPolynomialCalculator(gFLogic);
        this.errorLocator = new ErrorLocator(gFLogic);
        this.magnitudeSearcher = new MagnitudeSearcher(gFLogic);
    }


    @Override
    public char[] decodeMessage(char[] messageIn, int nSym, PolynomialArithmetic polynomialArithmetic) {
        Polynomial corrupted = new Polynomial(messageIn, gFLogic);
        Polynomial syndrom = sundromes.calcSyndromes(corrupted, nSym);

        if (syndrom.isZeroArray()) {
            return CharToHexRepr.toChar(corrupted.getWithoutPrefix(nSym));
        }

        Polynomial errLocatorPol = errorLocator.findErrorLocator(syndrom, nSym);
        var errorPositions = errorLocator.findPositions(errLocatorPol, messageIn.length);
        var magnitude = magnitudeSearcher.findMagnitude(errorPositions, syndrom, errLocatorPol, nSym, messageIn.length);
        var decodedPolynomial = magnitudeSearcher.findRealPolynomial(magnitude, corrupted, nSym);
        return decodedPolynomial.toCharArray();
    }


}
