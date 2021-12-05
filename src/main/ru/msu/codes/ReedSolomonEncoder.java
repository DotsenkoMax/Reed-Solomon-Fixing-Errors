package ru.msu.codes;

public interface ReedSolomonEncoder {

    char[] genMessageAndRsCode(char[] messageIn, int nSym,  PolynomialArithmetic polynomialArithmetic);
}
