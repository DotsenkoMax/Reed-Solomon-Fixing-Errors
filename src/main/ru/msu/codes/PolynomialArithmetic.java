package ru.msu.codes;

public class PolynomialArithmetic {
    public final GaluaFieldAriphmetic gFLogic;
    public Polynomial generatorPolynomial;

    public PolynomialArithmetic(GaluaFieldAriphmetic gFLogic) {
        this.gFLogic = gFLogic;
    }

    public void initGeneratorPolynomial(int nSym) {
        // (x+a^1)*...(x+a^(nSym))
        generatorPolynomial = new Polynomial(new int[]{1}, gFLogic);
        for (int i = 1; i < nSym+1; ++i) {
            Polynomial fractal = new Polynomial(new int[]{gFLogic.getAlphaInDeg(i), 1}, gFLogic);
            generatorPolynomial.multInPlace(fractal);    // x + alpha^{i} = [alpha^{i}, 1]
        }
    }

}
