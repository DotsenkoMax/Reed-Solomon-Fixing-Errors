package ru.msu.codes;

/**
 * Реализация поля Галуа и его арифметики
 */
public class GaluaFieldArithmetic {

    /**
     * Размерность поля Галуа
     */
    public final int galuaFieldDim;
    /**
     * Модуль степени
     */
    public final int mod;
    /**
     * Неприводимый полином
     */
    public final int irreduciblePolynomial;
    /**
     * Генератор поля
     */
    public final int alpha;
    /**
     * Степень alpha к элементу поля
     */
    public final int[] degreeIdx2GFValue;
    /**
     * Элемент поля к степени alpha
     */
    public final int[] gFValue2degreeIdx;

    public GaluaFieldArithmetic(int galuaFieldDim, int irreduciblePolynomial, int generatorNumber) {
        this.galuaFieldDim = galuaFieldDim;
        this.mod = galuaFieldDim - 1;
        this.irreduciblePolynomial = irreduciblePolynomial;
        this.alpha = generatorNumber;
        this.degreeIdx2GFValue = new int[galuaFieldDim];
        this.gFValue2degreeIdx = new int[galuaFieldDim];
    }

    /**
     * Инициализация массивов для сопоставления
     * степеней генератора поля и элементов поля
     * <p>
     * Для произведения элементов поля используется
     * Russian Peasant Multiplication Algorithm
     */
    public void initAlphaTable() {
        degreeIdx2GFValue[0] = 1;
        for (int i = 1; i < degreeIdx2GFValue.length; i++) {
            degreeIdx2GFValue[i] = multiplyRussianPeasantMultiplicationAlgo(degreeIdx2GFValue[i - 1], alpha);
        }

        for (int i = 0; i < gFValue2degreeIdx.length; i++) {
            gFValue2degreeIdx[degreeIdx2GFValue[i]] = i;
        }
        gFValue2degreeIdx[degreeIdx2GFValue[galuaFieldDim - 1]] = 0;
    }


    public int multiply(int lhs, int rhs) {
        assert lhs < galuaFieldDim && rhs < galuaFieldDim;
        if (lhs * rhs == 0) {
            return 0;
        }
        var idxResult = (gFValue2degreeIdx[lhs] + gFValue2degreeIdx[rhs]) % mod;
        return degreeIdx2GFValue[idxResult];
    }

    public int inverse(int value) {
        return divide(1, value);
    }

    public int divide(int lhs, int rhs) {
        assert lhs < galuaFieldDim && rhs < galuaFieldDim;
        assert rhs != 0;
        if (lhs * rhs == 0) {
            return 0;
        }
        var idxResult = ((gFValue2degreeIdx[lhs] - gFValue2degreeIdx[rhs]) % mod + mod) % mod;
        return degreeIdx2GFValue[idxResult];
    }

    public int sum(int lhs, int rhs) {
        assert lhs < galuaFieldDim && rhs < galuaFieldDim;
        return lhs ^ rhs;
    }

    public int subtract(int lhs, int rhs) {
        assert lhs < galuaFieldDim && rhs < galuaFieldDim;
        return lhs ^ rhs;
    }

    public int getAlphaInDeg(int i) {
        return degreeIdx2GFValue[(i % mod + mod) % mod];
    }


    protected int multiplyRussianPeasantMultiplicationAlgo(int lhs, int rhs) {
        int remainder = 0;
        while (rhs > 0) {
            if ((rhs & 1) != 0) {
                remainder ^= lhs;
            }
            rhs >>= 1;
            lhs <<= 1;

            if ((lhs & galuaFieldDim) != 0) {
                lhs ^= irreduciblePolynomial;
            }
        }
        return remainder;
    }
}