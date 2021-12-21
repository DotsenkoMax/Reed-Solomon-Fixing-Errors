package ru.msu.codes.decoder;

import ru.msu.codes.GaluaFieldArithmetic;
import ru.msu.codes.Polynomial;

import java.util.List;

/**
 * Генератор полинома амплитуд ошибок
 * <p>
 * Значения, которые нужно прибавить к искажённым символам сообщения,
 * чтобы получились неискажённые символы
 */
public class AmplitudeService {
    GaluaFieldArithmetic gFLogic;

    public AmplitudeService(GaluaFieldArithmetic gFLogic) {
        this.gFLogic = gFLogic;
    }

    /**
     * @param magnitude полином амплитуд ошибок
     * @param corrupted полином полученного сообщения
     * @return полином сообщения после исправления ошибок
     */
    public Polynomial findRealPolynomial(Polynomial magnitude, Polynomial corrupted) {
        assert magnitude.degree() == magnitude.degree();
        return magnitude.sum(corrupted);
    }

    /**
     * Метод рассчитывает амплитуды ошибок.
     * Для этого пользуемся алгоритмом Форни.
     *
     * @param errorIndices      индексы ошибок в сообщении
     * @param syndrome          полином синдромов
     * @param errorLocator      полином-локатор
     * @param nSym              количество дополнительных символов
     * @param encodedMessageLen длина закодированного сообщения
     * @return полином амплитуд ошибок
     */
    public Polynomial findMagnitude(List<Integer> errorIndices, Polynomial syndrome, Polynomial errorLocator, int nSym, int encodedMessageLen) {
        /** Полином ошибок = (def) syndrome * errorLocator */
        Polynomial nominator = syndrome.mult(errorLocator).getOnlyPrefix(nSym);
        Polynomial derivativeErrLocator = errorLocator.deriveFormal();
        int[] magnitude = new int[encodedMessageLen];
        for (var idx : errorIndices) {
            var reversedAlphaInPositionDegree = gFLogic.inverse(gFLogic.getAlphaInDeg(idx));
            var errValue = nominator.eval(reversedAlphaInPositionDegree);
            var derValue = derivativeErrLocator.eval(reversedAlphaInPositionDegree);
            var currMagnitude = gFLogic.divide(errValue, derValue);
            magnitude[idx] = currMagnitude;
        }
        return new Polynomial(magnitude, gFLogic);
    }
}
