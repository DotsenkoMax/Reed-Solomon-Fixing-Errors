package ru.msu.codes.decoder;

import ru.msu.codes.GaluaFieldArithmetic;
import ru.msu.codes.Polynomial;
import ru.msu.codes.PolynomialArithmetic;

public class ReedSolomonDecoderImpl implements ReedSolomonDecoder {
    public final GaluaFieldArithmetic gFLogic;
    public final SyndromesService sundromes;
    public final ErrorLocatorService errorLocatorService;
    public final AmplitudeService amplitudeService;

    public ReedSolomonDecoderImpl(GaluaFieldArithmetic gFLogic) {
        this.gFLogic = gFLogic;
        this.sundromes = new SyndromesService(gFLogic);
        this.errorLocatorService = new ErrorLocatorService(gFLogic);
        this.amplitudeService = new AmplitudeService(gFLogic);
    }


    @Override
    public int[] decodeMessage(int[] messageIn, int nSym, PolynomialArithmetic polynomialArithmetic) {
        Polynomial corrupted = new Polynomial(messageIn, gFLogic);
        Polynomial syndrome = sundromes.calcSyndromes(corrupted, nSym);

        if (syndrome.isZeroArray()) {
            return corrupted.deletePrefix(nSym); // Deleting error correction symbols
        }

        var errLocatorPol = errorLocatorService.findErrorLocator(syndrome, nSym); // Berlekamp–Massey
        var errorPositions = errorLocatorService.findPositions(errLocatorPol, messageIn.length);
        var magnitude = amplitudeService.findMagnitude(errorPositions, syndrome, errLocatorPol, nSym, messageIn.length);
        var decodedPolynomial = amplitudeService.findRealPolynomial(magnitude, corrupted);
        return decodedPolynomial.deletePrefix(nSym); // Deleting error correction symbols
    }


}
