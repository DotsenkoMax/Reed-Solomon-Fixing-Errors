package ru.msu.codes.decoder;

import ru.msu.codes.GaluaFieldArithmetic;
import ru.msu.codes.Polynomial;

import java.util.ArrayList;

/**
 * Генератор полинома-локатора
 * полином, корнями которого являются числа обратные примитивному члену в степени позиции ошибки
 */
public class ErrorLocatorService {
    GaluaFieldArithmetic gFLogic;

    public ErrorLocatorService(GaluaFieldArithmetic gFLogic) {
        this.gFLogic = gFLogic;
    }


    /**
     * Нахождения полинома-локатора
     * (1 + x * a^{pos_1}) * ... * (1 + x * a^{pos_k})
     * Berlekamp–Massey algo
     *
     * @param syndrome полином синдромов
     * @param nSym количество избыточных символов
     * @return полином - локатор
     */
    public Polynomial findErrorLocator(Polynomial syndrome, int nSym) {
        var locators = new Polynomial(new int[]{1}, gFLogic);
        var oldLocators = new Polynomial(new int[]{1}, gFLogic);
        for (int i = 0; i < nSym; i++) {
            int delta = syndrome.get(i);
            for (int j = 1; j <= locators.degree(); j++) {
                delta = gFLogic.sum(delta, gFLogic.multiply(locators.get(j), syndrome.get(i - j)));
            }
            oldLocators = oldLocators.appendLeft(new Polynomial(new int[]{0}, gFLogic));
            if (delta != 0) {
                if (oldLocators.degree() > locators.degree()) {
                    Polynomial newLocators = oldLocators.multConst(delta);
                    oldLocators = locators.multConst(gFLogic.inverse(delta));
                    locators = newLocators;
                }
                locators = locators.sum(oldLocators.multConst(delta));
            }
        }
        assert locators.degree() * 2 <= nSym;
        return locators;
    }

    /**
     * Метод ищет корни многочлена и по ним определяет позиции ошибок в сообщении
     * @param errLocatorPol полином-локатор
     * @param msgLen        длина сообщения
     * @return позиции ошибок
     */
    public ArrayList<Integer> findPositions(Polynomial errLocatorPol, int msgLen) {
        ArrayList<Integer> positions = new ArrayList<>();
        int totalErrs = errLocatorPol.degree(), errsFound = 0;
        for (int i = 0; i < msgLen; i++) {
            if (errLocatorPol.eval(gFLogic.inverse(gFLogic.getAlphaInDeg(i))) == 0) {
                positions.add(i);
                errsFound += 1;
            }
        }
        positions.sort(Integer::compareTo);

        assert errsFound == totalErrs;
        return positions;

    }
}
