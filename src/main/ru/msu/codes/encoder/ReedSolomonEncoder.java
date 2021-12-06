package ru.msu.codes.encoder;

import ru.msu.codes.PolynomialArithmetic;

public interface ReedSolomonEncoder {

    int[] genMessageAndRsCode(int[] messageIn, int nSym,  PolynomialArithmetic polynomialArithmetic);
}
