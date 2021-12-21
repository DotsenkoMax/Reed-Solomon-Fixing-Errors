package ru.msu.codes.decoder;

import ru.msu.codes.PolynomialArithmetic;

/**
 * Декодирование сообщений с исправлением ошибок Рида-Соломона
 */
public interface ReedSolomonDecoder {

    /**
     * @param messageIn            закодированное сообщение
     * @param nSym                 количество избыточных символов
     * @param polynomialArithmetic класс полинома-генератора
     * @return числовое представление декодированного сообщения
     */
    int[] decodeMessage(int[] messageIn, int nSym, PolynomialArithmetic polynomialArithmetic);
}
