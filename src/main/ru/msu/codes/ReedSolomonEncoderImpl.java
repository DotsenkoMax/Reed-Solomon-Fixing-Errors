package ru.msu.codes;

public class ReedSolomonEncoderImpl implements ReedSolomonEncoder {
    public final GaluaFieldAriphmetic gFLogic;

    public ReedSolomonEncoderImpl(GaluaFieldAriphmetic gFLogic) {
        this.gFLogic = gFLogic;
    }

    @Override
    public char[] genMessageAndRsCode(char[] messageIn, int nSym, PolynomialArithmetic polynomialArithmetic) {
        Polynomial messagePolynomial = new Polynomial(messageIn, gFLogic);
        Polynomial zeroPolynomial = new Polynomial(nSym, gFLogic);

        Polynomial remainderPolynomial = messagePolynomial
                .appendWithShifting(zeroPolynomial)
                .divide(polynomialArithmetic.generatorPolynomial);
        return messagePolynomial.appendWithShifting(remainderPolynomial).toCharArray();
    }
}
