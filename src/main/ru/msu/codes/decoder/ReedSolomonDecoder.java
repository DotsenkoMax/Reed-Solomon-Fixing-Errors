package ru.msu.codes.decoder;

import ru.msu.codes.PolynomialArithmetic;

public interface ReedSolomonDecoder {

    int[] decodeMessage(int[] messageIn, int nSym, PolynomialArithmetic polynomialArithmetic);
}
