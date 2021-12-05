package ru.msu.codes;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.LinkedList;

public class Polynomial {
    private int[] pol;
    private final GaluaFieldAriphmetic gFLogic;

    public Polynomial(int[] pol, GaluaFieldAriphmetic gFLogic) {
        this.pol = pol;
        this.gFLogic = gFLogic;
    }

    public Polynomial(char[] pol, GaluaFieldAriphmetic gFLogic) {
        this.pol = CharToHexRepr.toInt(pol);
        this.gFLogic = gFLogic;
    }

    public Polynomial(int n, GaluaFieldAriphmetic gFLogic) {
        this.pol = new int[n];
        this.gFLogic = gFLogic;
    }

    public int degree() {
        return pol.length - 1;
    }

    public int get(int i) {
        return pol[i];
    }

    public void deleteLeadingZeroes() {
        int idx = 0;
        while (idx < pol.length && pol[idx] == 0) {
            ++idx;
        }
        if (idx == 0) return;
        int[] newPol = new int[pol.length - idx];
        System.arraycopy(pol, idx, newPol, 0, newPol.length);
        pol = newPol;
    }

    public void multInPlace(Polynomial rhs) {
        int[] new_pol = new int[rhs.degree() + degree() + 1];
        for (int j = 0; j < degree() + 1; j++) {
            for (int k = 0; k < rhs.degree() + 1; k++) {
                var multed = gFLogic.multiply(pol[j], rhs.pol[k]);
                new_pol[k + j] = gFLogic.sum(new_pol[k + j], multed);
            }
        }
        pol = new_pol;
    }

    public Polynomial mult(Polynomial rhs) {
        Polynomial newPol = new Polynomial(new int[rhs.degree() + degree() + 1], gFLogic);
        for (int j = 0; j < degree() + 1; j++) {
            for (int k = 0; k < rhs.degree() + 1; k++) {
                var multed = gFLogic.multiply(pol[j], rhs.pol[k]);
                newPol.pol[k + j] = gFLogic.sum(newPol.pol[k + j], multed);
            }
        }
        return newPol;
    }

    public Polynomial scale(int val) {
        Polynomial polynomial = new Polynomial(new int[this.degree() + 1], gFLogic);
        for (int i = 0; i <= this.degree(); i++) {
            polynomial.pol[i] = gFLogic.multiply(pol[i], val);
        }
        return polynomial;
    }

    public Polynomial sum(Polynomial rhs) {
        Polynomial polynomial = new Polynomial(new int[Math.max(rhs.degree() + 1, degree() + 1)], gFLogic);
        if (rhs.degree() > degree()) {
            if (rhs.degree() + 1 >= 0) System.arraycopy(rhs.pol, 0, polynomial.pol, 0, rhs.degree() + 1);
            int delta = rhs.degree() - degree();
            for (int i = 0; i < pol.length; i++) {
                polynomial.pol[delta + i] = gFLogic.sum(polynomial.pol[delta+i], pol[i]);
            }
        } else {
            if (degree() + 1 >= 0) System.arraycopy(pol, 0, polynomial.pol, 0, degree() + 1);
            int delta = degree() - rhs.degree();
            for (int i = 0; i <= rhs.degree(); i++) {
                polynomial.pol[delta + i] = gFLogic.sum(polynomial.pol[delta + i], rhs.pol[i]);
            }
        }
        return polynomial;
    }

    public Polynomial sumReversed(Polynomial rhs) {
        Polynomial polynomial = new Polynomial(new int[Math.max(rhs.degree() + 1, degree() + 1)], gFLogic);
        if (rhs.degree() > degree()) {
            if (rhs.degree() + 1 >= 0) System.arraycopy(rhs.pol, 0, polynomial.pol, 0, rhs.degree() + 1);
            for (int i = 0; i < pol.length; i++) {
                polynomial.pol[i] = gFLogic.sum(polynomial.pol[i], pol[i]);
            }
        } else {
            if (degree() + 1 >= 0) System.arraycopy(pol, 0, polynomial.pol, 0, degree() + 1);
            for (int i = 0; i <= rhs.degree(); i++) {
                polynomial.pol[i] = gFLogic.sum(polynomial.pol[i], rhs.pol[i]);
            }
        }
        return polynomial;
    }

    public Polynomial append(Polynomial rhs) {
        int[] new_pol = new int[pol.length + rhs.pol.length];
        System.arraycopy(pol, 0, new_pol, 0, pol.length);
        System.arraycopy(rhs.pol, 0, new_pol, pol.length, rhs.pol.length);
        return new Polynomial(new_pol, gFLogic);
    }

    public Polynomial divide(Polynomial rhs) {
        var clonedPol = pol.clone();
        for (int i = 0; i < clonedPol.length - rhs.pol.length + 1; i++) {
            var coef = clonedPol[i];
            if (coef != 0) {
                for (int j = 1; j < rhs.pol.length; j += 1) {
                    if (rhs.pol[j] != 0) {
                        clonedPol[i + j] = gFLogic.sum(clonedPol[i + j], gFLogic.multiply(rhs.pol[j], coef));
                    }
                }
            }
        }
        var cnt = rhs.pol.length - 1;
        int[] lastCntBytes = new int[cnt];
        System.arraycopy(clonedPol, clonedPol.length - cnt, lastCntBytes, 0, lastCntBytes.length);
        return new Polynomial(lastCntBytes, gFLogic);
    }

    public int eval(int x) {
        assert x < gFLogic.galuaFieldDim;
        var ans = 0;
        for (var each : pol) {
            ans = gFLogic.multiply(ans, x);
            ans = gFLogic.sum(ans, each);
        }
        return ans;
    }

    public int evalReverse(int x) {
        assert x < gFLogic.galuaFieldDim;
        var ans = 0;
        for (int i = pol.length - 1; i >= 0; i--) {
            ans = gFLogic.multiply(ans, x);
            ans = gFLogic.sum(ans, pol[i]);
        }
        return ans;
    }

    public Polynomial reverse() {
        int[] arr = pol.clone();
        for (int i = 0, j = arr.length - 1; i < arr.length / 2; i++, j--) {
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        return new Polynomial(arr, gFLogic);
    }

    public char[] toCharArray() {
        return CharToHexRepr.toChar(pol);
    }

    public int[] toIntArray() {
        return pol;
    }

    public int[] getWithoutSuffix(int nSym) {
        return Arrays.copyOfRange(pol, 0, pol.length - nSym);
    }
    public Polynomial getPolWithoutSuffix(int nSym) {
        return new Polynomial(Arrays.copyOfRange(pol, 0, pol.length - nSym), gFLogic);
    }

    public Polynomial getOnlySuffix(int nSym) {
        return new Polynomial(Arrays.copyOfRange(pol, pol.length - nSym, pol.length), gFLogic);
    }

    public boolean isZeroArray() {
        return Arrays.stream(pol).sum() == 0;
    }

    /**
     * (c*x^n)' [in GF(2)] = c * (n mod 2) * x^(n-1)
     * delete all evens
     */
    public Polynomial deriveFormal() {
        LinkedList<Integer> tmp = new LinkedList<>();
        for (int i = degree() - 1; i >= 0; i -= 2) {
            tmp.addFirst(pol[i]);
            tmp.addFirst(0);
        }
        tmp.pollFirst();
        return new Polynomial(ArrayUtils.toPrimitive(tmp.toArray(new Integer[0])), gFLogic);
    }

}
