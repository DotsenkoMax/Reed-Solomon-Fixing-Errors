package ru.msu.codes;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Полиномы над полем Галуа
 * в порядке убывания степеней членов
 */
public class Polynomial {
    private ArrayList<Integer> pol;
    private final GaluaFieldArithmetic gFLogic;

    public Polynomial(int[] pol, GaluaFieldArithmetic gFLogic) {
        this.pol = new ArrayList<>();
        for (int j : pol) {
            this.pol.add(j);
        }
        this.gFLogic = gFLogic;
    }

    public Polynomial(ArrayList<Integer> pol, GaluaFieldArithmetic gFLogic) {
        this.pol = pol;
        this.gFLogic = gFLogic;
    }

    public Polynomial(char[] pol, GaluaFieldArithmetic gFLogic) {
        this(CharToHexRepr.toInt(pol), gFLogic);
    }

    public Polynomial(int n, GaluaFieldArithmetic gFLogic) {
        this(new int[n], gFLogic);
    }

    public int degree() {
        return pol.size() - 1;
    }

    public int get(int i) {
        return pol.get(i);
    }

    private void fill(ArrayList<Integer> ar, int sz) {
        for (int i = 0; i < sz; i++) {
            ar.add(0);
        }
    }

    public void multInPlace(Polynomial rhs) {
        ArrayList<Integer> ar = new ArrayList<>();
        fill(ar, rhs.degree() + degree() + 1);

        for (int j = 0; j < degree() + 1; j++) {
            for (int k = 0; k < rhs.degree() + 1; k++) {
                var multed = gFLogic.multiply(pol.get(j), rhs.pol.get(k));
                ar.set(k + j, gFLogic.sum(ar.get(k + j), multed));
            }
        }
        pol = ar;
    }

    public Polynomial mult(Polynomial rhs) {
        ArrayList<Integer> ar = new ArrayList<>();
        fill(ar, rhs.degree() + degree() + 1);
        Polynomial newPol = new Polynomial(ar, gFLogic);
        for (int j = 0; j < degree() + 1; j++) {
            for (int k = 0; k < rhs.degree() + 1; k++) {
                var multed = gFLogic.multiply(pol.get(j), rhs.pol.get(k));
                newPol.pol.set(k + j, gFLogic.sum(newPol.pol.get(k + j), multed));
            }
        }
        return newPol;
    }

    public Polynomial multConst(int val) {
        Polynomial polynomial = new Polynomial(new int[this.degree() + 1], gFLogic);
        for (int i = 0; i <= this.degree(); i++) {
            polynomial.pol.set(i, gFLogic.multiply(pol.get(i), val));
        }
        return polynomial;
    }

    public Polynomial sum(Polynomial rhs) {
        Polynomial polynomial = new Polynomial(new int[Math.max(rhs.degree() + 1, degree() + 1)], gFLogic);
        for (int i = 0; i < polynomial.degree() + 1; i++) {
            if (i < pol.size()) {
                polynomial.pol.set(i, pol.get(i));
            }
            if (i < rhs.pol.size()) {
                polynomial.pol.set(i, gFLogic.sum(polynomial.pol.get(i), rhs.pol.get(i)));
            }
        }
        return polynomial;
    }

    /**
     * (this, rhs) -> Pol([rhs , this])
     *
     * @param rhs полином, который приписываем слева к нашему
     */
    public Polynomial appendLeft(Polynomial rhs) {
        ArrayList<Integer> ar = new ArrayList<>(degree() + rhs.degree() + 2);
        ar.addAll(rhs.pol);
        ar.addAll(pol);
        return new Polynomial(ar, gFLogic);
    }

    public Polynomial divide(Polynomial rhs) {
        Polynomial rhsReversed = rhs.reverse();
        ArrayList<Integer> clonedPol = this.reverse().pol;
        for (int i = 0; i < clonedPol.size() - rhsReversed.pol.size() + 1; i++) {
            var coef = gFLogic.divide(clonedPol.get(i), rhsReversed.get(0));
            if (coef != 0) {
                for (int j = 0; j < rhsReversed.pol.size(); j += 1) {
                    if (rhsReversed.pol.get(j) != 0) {
                        clonedPol.set(i + j, gFLogic.subtract(clonedPol.get(i + j), gFLogic.multiply(rhsReversed.pol.get(j), coef)));
                    }
                }
            }
        }
        var cnt = rhsReversed.degree();
        Collections.reverse(clonedPol);
        int deleteSize = clonedPol.size() - cnt;
        for (int i = 0; i < deleteSize; i++) {
            clonedPol.remove(clonedPol.size() - 1);
        }
        return new Polynomial(clonedPol, gFLogic);
    }

    /**
     * @param x переменная для подстанвки в полином
     * @return значение полинома от данной переменной
     */
    public int eval(int x) {
        assert x < gFLogic.galuaFieldDim;
        var ans = 0;
        for (int i = degree(); i >= 0; i--) {
            ans = gFLogic.multiply(ans, x);
            ans = gFLogic.sum(ans, pol.get(i));
        }
        return ans;
    }

    public Polynomial reverse() {
        ArrayList<Integer> clonedPol = (ArrayList<Integer>) pol.clone();
        Collections.reverse(clonedPol);
        return new Polynomial(clonedPol, gFLogic);
    }

    public char[] toCharArray() {
        return CharToHexRepr.toChar(ArrayUtils.toPrimitive(pol.toArray(new Integer[0])));
    }

    public int[] toIntArray() {
        return ArrayUtils.toPrimitive(pol.toArray(new Integer[0]));
    }

    public int[] deletePrefix(int nSym) {
        int[] arr = new int[pol.size() - nSym];
        for (int i = nSym; i < pol.size(); i++) {
            arr[i - nSym] = pol.get(i);
        }
        return arr;
    }

    public Polynomial getOnlyPrefix(int nSym) {
        ArrayList<Integer> clonedPol = (ArrayList<Integer>) pol.clone();
        while (clonedPol.size() != nSym) {
            clonedPol.remove(clonedPol.size() - 1);
        }
        return new Polynomial(clonedPol, gFLogic);
    }

    public Polynomial deletePolynomPrefix(int nSym) {
        ArrayList<Integer> clonedPol = reverse().pol;
        var realLength = clonedPol.size() - nSym;
        while (clonedPol.size() != realLength) {
            clonedPol.remove(clonedPol.size() - 1);
        }
        Collections.reverse(clonedPol);
        return new Polynomial(clonedPol, gFLogic);
    }

    public boolean isZeroArray() {
        return pol.stream().filter(a -> a == 0).count() == pol.size();
    }

    /**
     * Производная от многочлена
     * (c*x^n)' [in GF(2)] = c * (n mod 2) * x^(n-1)
     * удачение четных степеней
     */
    public Polynomial deriveFormal() {
        ArrayList<Integer> clonedPol = (ArrayList<Integer>) pol.clone();
        clonedPol.remove(0);
        for (int i = 1; i < clonedPol.size(); i += 2) {
            clonedPol.set(i, 0);
        }
        return new Polynomial(clonedPol, gFLogic);
    }

}
