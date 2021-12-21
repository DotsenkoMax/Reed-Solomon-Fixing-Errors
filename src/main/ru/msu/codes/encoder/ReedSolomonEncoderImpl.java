package ru.msu.codes.encoder;

import ru.msu.codes.GaluaFieldArithmetic;
import ru.msu.codes.Polynomial;
import ru.msu.codes.PolynomialArithmetic;

public class ReedSolomonEncoderImpl implements ReedSolomonEncoder {

    public final GaluaFieldArithmetic gFLogic;

    public ReedSolomonEncoderImpl(GaluaFieldArithmetic gFLogic) {
        this.gFLogic = gFLogic;
    }

    @Override
    public int[] genMessageAndRsCode(int[] messageIn, int nSym, PolynomialArithmetic polynomialArithmetic) {
        Polynomial message = new Polynomial(messageIn, gFLogic);
        Polynomial zero = new Polynomial(nSym, gFLogic);

        Polynomial remainderPolynomial = message
                .appendLeft(zero)
                .divide(polynomialArithmetic.generatorPolynomial);
        return message.appendLeft(remainderPolynomial).toIntArray();
    }
}
