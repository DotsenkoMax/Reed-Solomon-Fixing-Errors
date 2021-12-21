package ru.msu.codes.encoder;

import ru.msu.codes.PolynomialArithmetic;

public interface ReedSolomonEncoder {

    /**
     *
     * @param messageIn сообщение для кодирования
     * @param nSym максимальное допустимое число ошибок
     * @param polynomialArithmetic хранение полинома-генератора
     * @return закодированное сообщение
     */
    int[] genMessageAndRsCode(int[] messageIn, int nSym,  PolynomialArithmetic polynomialArithmetic);
}
