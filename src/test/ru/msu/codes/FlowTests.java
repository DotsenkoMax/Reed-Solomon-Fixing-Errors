package ru.msu.codes;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.msu.codes.decoder.ErrorLocator;
import ru.msu.codes.decoder.MagnitudeSearcher;
import ru.msu.codes.decoder.SyndromesPolynomialCalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlowTests {

    @Test
    public void divideTest() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        Polynomial divisor = new Polynomial(new char[]{0, 0, 0, 0, 0x44, 0x4F, 0x4E, 0x27, 0x54, 0x20, 0x50, 0x41, 0x4E, 0x49, 0x43}, gf); // 0, 0, 0, 0 DONT PANIC
        Polynomial divider = new Polynomial(new char[]{0x74, 0xE7, 0xD8, 0x1E, 0x01}, gf);
        Assertions.assertArrayEquals(new char[]{0xDB, 0x22, 0x58, 0x5C}, divisor.divide(divider).toCharArray());
    }

    @Test
    public void formalDeriveTest() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        Polynomial func = new Polynomial(new char[]{1, 45, 165, 198, 140, 223}, gf);
        Assertions.assertArrayEquals(new char[]{45, 0, 198, 0, 223}, func.deriveFormal().toCharArray());
    }

    @Test
    public void checkGenPolynom() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        int nSym = 4;
        var polArithm = new PolynomialArithmetic(gf);
        polArithm.initGeneratorPolynomial(nSym);
        Assertions.assertArrayEquals(new char[]{0x74, 0xE7, 0xD8, 0x1E, 0x01}, polArithm.generatorPolynomial.toCharArray());
    }

    @Test
    public void encodeTest() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        var encoder = new ReedSolomonEncoderImpl(gf);
        int nSym = 4;
        var polArithm = new PolynomialArithmetic(gf);
        polArithm.initGeneratorPolynomial(nSym);

        var initital = new int[]{0x44, 0x4F, 0x4E, 0x27, 0x54, 0x20, 0x50, 0x41, 0x4E, 0x49, 0x43}; // DONT PANIC
        var encoded = encoder.genMessageAndRsCode(CharToHexRepr.toChar(initital), nSym, polArithm);
        Assertions.assertArrayEquals(new char[]{
                        0xDB, 0x22, 0x58, 0x5C, 0x44, 0x4F, 0x4E, 0x27, 0x54, 0x20, 0x50, 0x41, 0x4E, 0x49, 0x43
                },
                encoded
        );
    }

    @Test
    public void syndromTest() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        var nSym = 4;
        char[] encodedMsg = new char[]{0xDB, 0x22, 0x58, 0x5C, 0x44, 0x4F, 0x4E, 0x27, 0x54, 0x20, 0x50, 0x41, 0x4E, 0x49, 0x43};
        SyndromesPolynomialCalculator syndromCalc = new SyndromesPolynomialCalculator(gf);
        assert syndromCalc.calcSyndromes(new Polynomial(encodedMsg, gf), nSym).isZeroArray();
        char[] corruptedMsg = new char[]{0xDB, 0x22, 0x58, 0x5C, 0x44, 0x4F, 0x4E, 0x27, 0x54, 0x20, 0x50, 0x41, 0x4E, 0x49, 0x42};
        char[] syndrome = syndromCalc.calcSyndromes(new Polynomial(corruptedMsg, gf), nSym).toCharArray();
        Assertions.assertArrayEquals(new char[]{0x13, 0x18, 0xB5, 0x5D}, syndrome);
    }

    @Test
    public void errorLocatorTest() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        SyndromesPolynomialCalculator syndromCalc = new SyndromesPolynomialCalculator(gf);
        ErrorLocator errorLocatorCalc = new ErrorLocator(gf);
        var nSym = 4;
        char[] encodedMsg = new char[]{0xDB, 0x22, 0x58, 0x5C, 0x44, 0x4F, 0x4E, 0x27, 0x54, 0x20, 0x50, 0x41, 0x4E, 0x49, 0x43};
        char[] corruptedMsg = new char[]{0x02, 0x22, 0x58, 0x5C, 0x44, 0x4F, 0x4E, 0x27, 0x54, 0x20, 0x50, 0x41, 0x4E, 0x49, 0x01};
        char[] syndrome = syndromCalc.calcSyndromes(new Polynomial(corruptedMsg, gf), nSym).toCharArray();
        Assertions.assertArrayEquals(new char[]{0x4b, 0xa7, 0xe8, 0xbd}, syndrome); // Also check Syndrome
        char[] errLocator = errorLocatorCalc.findErrorLocator(new Polynomial(syndrome, gf), nSym).toCharArray();
        Assertions.assertArrayEquals(new char[]{0x01, 0x12, 0x13}, errLocator); // Also check ErrLocator
    }

    @Test
    public void errorPositionsTest() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        SyndromesPolynomialCalculator syndromCalc = new SyndromesPolynomialCalculator(gf);
        ErrorLocator errorLocatorCalc = new ErrorLocator(gf);
        var nSym = 4;
        char[] encodedMsg = new char[]{0xDB, 0x22, 0x58, 0x5C, 0x44, 0x4F, 0x4E, 0x27, 0x54, 0x20, 0x50, 0x41, 0x4E, 0x49, 0x43};
        char[] corruptedMsg = new char[]{0x02, 0x22, 0x58, 0x5C, 0x44, 0x4F, 0x4E, 0x27, 0x54, 0x20, 0x50, 0x41, 0x4E, 0x49, 0x01};
        char[] syndrome = syndromCalc.calcSyndromes(new Polynomial(corruptedMsg, gf), nSym).toCharArray();
        Assertions.assertArrayEquals(new char[]{0x4b, 0xa7, 0xe8, 0xbd}, syndrome); // Also check Syndrome
        char[] errLocator = errorLocatorCalc.findErrorLocator(new Polynomial(syndrome, gf), nSym).toCharArray();
        Assertions.assertArrayEquals(new char[]{0x01, 0x12, 0x13}, errLocator); // Also check ErrLocator
        ArrayList<Integer> positions = errorLocatorCalc.findPositions(new Polynomial(errLocator, gf), encodedMsg.length);
        positions.sort(Integer::compareTo);
        Assertions.assertArrayEquals(new Integer[]{0, 14}, positions.toArray(new Integer[0])); // Also check ErrPositions
    }

    @Test
    public void magnitudeTest() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        char[] encodedMsg = new char[]{0xDB, 0x22, 0x58, 0x5C, 0x44, 0x4F, 0x4E, 0x27, 0x54, 0x20, 0x50, 0x41, 0x4E, 0x49, 0x43}; // DONTPANIC
        char[] corrupted = new char[]{0xDB, 0x22, 0x58, 0x5C, 0x44, 0x4F, 0x4E, 0x27, 0x54, 0x20, 0x50, 0x41, 0x4E, 0x41, 0x41};  // DONTAAAAA
        SyndromesPolynomialCalculator syndromCalc = new SyndromesPolynomialCalculator(gf);
        ErrorLocator errorLocatorCalc = new ErrorLocator(gf);
        MagnitudeSearcher magnitudeSearcher = new MagnitudeSearcher(gf);
        int nSym = 4;
        Polynomial syndrome = syndromCalc.calcSyndromes(new Polynomial(corrupted, gf), nSym);

        List<Integer> positionErrors = Arrays.asList(13, 14);
        Polynomial errorLocPol = errorLocatorCalc.findErrorLocator(syndrome, nSym);
        Polynomial magnitude = magnitudeSearcher.findMagnitude(positionErrors, syndrome, errorLocPol, nSym, encodedMsg.length);

        Assertions.assertArrayEquals(new char[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0x08, 0x02}, magnitude.toCharArray());
    }


    @Test
    public void e2ETest() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        char[] initialMsg = new char[]{0x44, 0x4F, 0x4E, 0x27, 0x54, 0x20, 0x50, 0x41, 0x4E, 0x49, 0x43};
        char[] encodedMsg = new char[]{0xDB, 0x22, 0x58, 0x5C, 0x44, 0x4F, 0x4E, 0x27, 0x54, 0x20, 0x50, 0x41, 0x4E, 0x49, 0x43}; // DONTPANIC
        char[] corrupted = new char[]{0xDB, 0x22, 0x58, 0x5C, 0x44, 0x4F, 0x4E, 0x27, 0x54, 0x20, 0x50, 0x41, 0x4E, 0x41, 0x41};  // DONTAAAAA
        SyndromesPolynomialCalculator syndromCalc = new SyndromesPolynomialCalculator(gf);
        ErrorLocator errorLocatorCalc = new ErrorLocator(gf);
        MagnitudeSearcher magnitudeSearcher = new MagnitudeSearcher(gf);
        int nSym = 4;
        Polynomial syndrome = syndromCalc.calcSyndromes(new Polynomial(corrupted, gf), nSym);

        List<Integer> positionErrors = Arrays.asList(13, 14);
        Polynomial errorLocPol = errorLocatorCalc.findErrorLocator(syndrome, nSym);
        Polynomial magnitude = magnitudeSearcher.findMagnitude(positionErrors, syndrome, errorLocPol, nSym, encodedMsg.length);

        Assertions.assertArrayEquals(new char[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0x08, 0x02}, magnitude.toCharArray());

        Assertions.assertArrayEquals(
                initialMsg,
                magnitudeSearcher.findRealPolynomial(magnitude, new Polynomial(corrupted, gf), nSym).toCharArray()
        );
    }


}
