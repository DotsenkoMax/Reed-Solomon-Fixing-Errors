package ru.msu.codes.decoder;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.msu.codes.GaluaFieldAriphmetic;
import ru.msu.codes.Polynomial;
import ru.msu.codes.PolynomialArithmetic;
import ru.msu.codes.ReedSolomonEncoderImpl;

import java.util.Arrays;
import java.util.Collections;

public class SyndromesPolynomialCalculatorTest {
    @Test
    public void test() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        var encoder = new ReedSolomonEncoderImpl(gf);
        int nSym = 10;
        var polArithm = new PolynomialArithmetic(gf);
        polArithm.initGeneratorPolynomial(nSym);
        var encodedMsg = encoder.genMessageAndRsCode(new char[]{0x40, 0xd2, 0x75, 0x47, 0x76, 0x17, 0x32, 0x06,
                0x27, 0x26, 0x96, 0xc6, 0xc6, 0x96, 0x70, 0xec}, nSym, polArithm);

        var synd = new SyndromesPolynomialCalculator(gf).calcSyndromes(new Polynomial(encodedMsg, gf), nSym);
        Assertions.assertArrayEquals(new int[nSym], synd.toIntArray());
    }

    @Test
    public void incorrectTest() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        var encoder = new ReedSolomonEncoderImpl(gf);
        int nSym = 10;
        var polArithm = new PolynomialArithmetic(gf);
        polArithm.initGeneratorPolynomial(nSym);
        var encodedMsg = encoder.genMessageAndRsCode(new char[]{0x40, 0xd2, 0x75, 0x47, 0x76, 0x17, 0x32, 0x06,
                0x27, 0x26, 0x96, 0xc6, 0xc6, 0x96, 0x70, 0xec}, nSym, polArithm);

        encodedMsg[0] = 0;
        var synd = new SyndromesPolynomialCalculator(gf).calcSyndromes(new Polynomial(encodedMsg, gf), nSym);
        var expectedMsg = new Character[]{64, 192, 93, 231, 52, 92, 228, 49, 83, 245};
        Collections.reverse(Arrays.asList(expectedMsg));

        Assertions.assertArrayEquals(ArrayUtils.toPrimitive(expectedMsg), synd.toCharArray());
    }

    @Test
    public void smallBadTest() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        var encoder = new ReedSolomonEncoderImpl(gf);
        int nSym = 1;
        var polArithm = new PolynomialArithmetic(gf);
        polArithm.initGeneratorPolynomial(nSym);
        var encodedMsg = encoder.genMessageAndRsCode(new char[]{0x40}, nSym, polArithm);
        Assertions.assertEquals(nSym + 1, encodedMsg.length);
        encodedMsg[0] = 0x3;
        var synd = new SyndromesPolynomialCalculator(gf);
        Assertions.assertNotEquals(0,
                Arrays.stream(synd.calcSyndromes(new Polynomial(encodedMsg, gf), nSym).toIntArray()).sum());
    }
}
