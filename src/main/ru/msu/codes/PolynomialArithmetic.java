package ru.msu.codes;

public class PolynomialArithmetic {
    public final GaluaFieldArithmetic gFLogic;
    public Polynomial generatorPolynomial;

    public PolynomialArithmetic(GaluaFieldArithmetic gFLogic) {
        this.gFLogic = gFLogic;
    }

    public void initGeneratorPolynomial(int nRedundantSym) {
        /**
         * (x+a^1)*...(x+a^(nRedundantSym))
         */
        generatorPolynomial = new Polynomial(new int[]{1}, gFLogic);
        for (int i = 1; i < nRedundantSym + 1; ++i) {
            Polynomial fractal = new Polynomial(new int[]{gFLogic.getAlphaInDeg(i), 1}, gFLogic);
            generatorPolynomial.multInPlace(fractal);    // x + alpha^{i} = [alpha^{i}, 1]
        }
    }

}
