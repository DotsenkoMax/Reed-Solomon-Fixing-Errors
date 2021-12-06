package ru.msu.codes;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;

public class ReedSolomonEncoderImplTest {
    @Test
    public void test() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        var encoder = new ReedSolomonEncoderImpl(gf);
        int nSym = 10;
        var polArithm = new PolynomialArithmetic(gf);
        polArithm.initGeneratorPolynomial(nSym);
        var initital = new int[]{0x40, 0xd2, 0x75, 0x47, 0x76, 0x17, 0x32, 0x06,
                0x27, 0x26, 0x96, 0xc6, 0xc6, 0x96, 0x70, 0xec};
        var encoded = encoder.genMessageAndRsCode(CharToHexRepr.toChar(initital), nSym, polArithm);
        System.out.println(Arrays.toString(CharToHexRepr.toInt(encoded)));
        System.out.println(Arrays.toString(initital));
        Assertions.assertArrayEquals(new char[]{
                        0xbc, 0x2a, 0x90, 0x13, 0x6b, 0xaf, 0xef, 0xfd, 0x4b, 0xe0,
                        0x40, 0xd2, 0x75, 0x47, 0x76, 0x17, 0x32, 0x6, 0x27, 0x26, 0x96, 0xc6, 0xc6, 0x96, 0x70, 0xec
                },
                encoder.genMessageAndRsCode(new char[]{0x40, 0xd2, 0x75, 0x47, 0x76, 0x17, 0x32, 0x06,
                        0x27, 0x26, 0x96, 0xc6, 0xc6, 0x96, 0x70, 0xec}, nSym, polArithm)
        );
    }

    @Test
    public void test2() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        var encoder = new ReedSolomonEncoderImpl(gf);
        int nSym = 4;
        var polArithm = new PolynomialArithmetic(gf);
        polArithm.initGeneratorPolynomial(nSym);

        var initital = new int[]{0x44, 0x4F, 0x4E, 0x27, 0x54, 0x20, 0x50, 0x41, 0x4E, 0x49, 0x43};
        var encoded = encoder.genMessageAndRsCode(CharToHexRepr.toChar(initital), nSym, polArithm);
        System.out.println(Arrays.toString(CharToHexRepr.toInt(encoded)));
        System.out.println(Arrays.toString(initital));
        System.out.println(Arrays.toString(new int[]{0xDB, 0x22, 0x58, 0x5C}));
        Assertions.assertArrayEquals(new char[]{
                        0xDB, 0x22, 0x58, 0x5C, 0x44, 0x4F, 0x4E, 0x27, 0x54, 0x20, 0x50, 0x41, 0x4E, 0x49, 0x43
                },
                encoded
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
                new char[]{0xd9, 0x78, 0xe6, 0x37, 0x56, 0x34, 0x12},
                encoder.genMessageAndRsCode(new char[]{0x56, 0x34, 0x12}, nSym, polArithm)
        );
    }

    @Test
    public void hugeTest() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        int nSym = 24;
        var polArithm = new PolynomialArithmetic(gf);
        polArithm.initGeneratorPolynomial(nSym);
        var encoder = new ReedSolomonEncoderImpl(gf);
        encoder.genMessageAndRsCode(new char[230], nSym, polArithm);
    }
}
