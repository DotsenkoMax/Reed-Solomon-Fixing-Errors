package ru.msu.codes.decoder;

import ru.msu.codes.PolynomialArithmetic;

public interface ReedSolomonDecoder {

    char[] decodeMessage(char[] messageIn, int nSym, PolynomialArithmetic polynomialArithmetic);
}
