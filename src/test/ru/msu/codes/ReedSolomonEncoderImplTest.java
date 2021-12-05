package ru.msu.codes;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class ReedSolomonEncoderImplTest {
    @Test
    public void test() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        var encoder = new ReedSolomonEncoderImpl(gf);
        int nSym = 10;
        var polArithm = new PolynomialArithmetic(gf);
        polArithm.initGeneratorPolynomial(nSym);

        Assertions.assertArrayEquals(new char[]{
                        0x40, 0xd2, 0x75, 0x47, 0x76, 0x17, 0x32, 0x6, 0x27, 0x26, 0x96, 0xc6, 0xc6, 0x96, 0x70, 0xec,
                        0xbc, 0x2a, 0x90, 0x13, 0x6b, 0xaf, 0xef, 0xfd, 0x4b, 0xe0
                },
                encoder.genMessageAndRsCode(new char[]{0x40, 0xd2, 0x75, 0x47, 0x76, 0x17, 0x32, 0x06,
                        0x27, 0x26, 0x96, 0xc6, 0xc6, 0x96, 0x70, 0xec}, nSym, polArithm)
        );
    }

    @Test
    public void simpleTest() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        var encoder = new ReedSolomonEncoderImpl(gf);
        int nSym = 4;
        var polArithm = new PolynomialArithmetic(gf);
        polArithm.initGeneratorPolynomial(nSym);
        Assertions.assertArrayEquals(
                new char[]{0x12, 0x34, 0x56, 0x37, 0xe6, 0x78, 0xd9},
                encoder.genMessageAndRsCode(new char[]{0x12, 0x34, 0x56}, nSym, polArithm)
        );
    }

    @Test
    public void hugeTest(){
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        int nSym = 24;
        var polArithm = new PolynomialArithmetic(gf);
        polArithm.initGeneratorPolynomial(nSym);
        var encoder = new ReedSolomonEncoderImpl(gf);
        encoder.genMessageAndRsCode(new char[230], nSym, polArithm);
    }
}
