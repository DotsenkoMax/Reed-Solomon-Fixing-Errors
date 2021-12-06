package ru.msu.codes;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class PolynomialArithmeticTest {
    @Test
    public void test() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        var polArithm = new PolynomialArithmetic(gf);
        polArithm.initGeneratorPolynomial(4);
        Assertions.assertArrayEquals(
                new char[]{0x40, 0x78, 0x36, 0x0f, 0x01}, polArithm.generatorPolynomial.toCharArray()
        );
    }
}
