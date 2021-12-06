package ru.msu.codes;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.msu.codes.decoder.ErrorLocator;
import ru.msu.codes.decoder.MagnitudeSearcher;
import ru.msu.codes.decoder.SyndromesPolynomialCalculator;

import java.util.Arrays;
import java.util.Random;

public class IntegrationTest {
    @Test
    public void syndTest() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();


        char[] realMsg = new char[]{0xdb, 0x22, 0x58, 0x5C, 0x44, 0x4F, 0x4E, 0x27, 0x54, 0x20, 0x50, /*0x41, 0x4E, 0x49, 0x43*/};
        char[] corruptedMsg = new char[]{0x02, 0x22, 0x58, 0x5C, 0x44, 0x4F, 0x4E, 0x27, 0x54, 0x20, 0x50, 0x41, 0x4E, 0x49, 0x01};
        var syndromCalc = new SyndromesPolynomialCalculator(gf);
        var errLocatorCalc = new ErrorLocator(gf);
        var magnitudeCalc = new MagnitudeSearcher(gf);
        var synd = syndromCalc.calcSyndromes(new Polynomial(corruptedMsg, gf).reverse(), 4);
        var errorLocator = errLocatorCalc.findErrorLocator(synd, 4);
        var errors = errLocatorCalc.findPositions(errorLocator, corruptedMsg.length);
        var magnitude = magnitudeCalc.findMagnitude(errors, synd, errorLocator, 4, corruptedMsg.length);
        Assertions.assertArrayEquals(
                new char[]{0x4B, 0xA7, 0xE8, 0xBD}, synd.reverse().toCharArray()
        );
        var decoded = magnitudeCalc.findRealPolynomial(magnitude, new Polynomial(corruptedMsg, gf), 4);
        Assertions.assertArrayEquals(
                realMsg, decoded.toCharArray()
        );
    }

    @Test
    public void smallTest() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        var nSym = 2;
        var polArithm = new PolynomialArithmetic(gf);
        polArithm.initGeneratorPolynomial(nSym);
        var engine = new ReedSolomonEngineImpl(gf);
        var encoder = engine.getEncoder();
        var decoder = engine.getDecoder();
        char[] inputMsg = {2};
        char[] encoded = encoder.genMessageAndRsCode(inputMsg, nSym, polArithm);
        encoded[0] = 0;
        char[] decoded = decoder.decodeMessage(encoded, nSym, polArithm);
        Assertions.assertArrayEquals(inputMsg, decoded);
    }

    @Test
    public void stressTest() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        var polArithm = new PolynomialArithmetic(gf);
        var engine = new ReedSolomonEngineImpl(gf);
        var encoder = engine.getEncoder();
        var decoder = engine.getDecoder();
        int iterations = 100000;
        var generator = new Random();
        while (iterations != 0) {
//          System.out.println("--------------------------------------------------------");
            int[] inputMsg = genRandomArray(80, generator);
            int nSym = (generator.nextInt(inputMsg.length) + 1) * 2;
            polArithm.initGeneratorPolynomial(nSym);
            var messageCorrupter = new MessageCorrupterImpl(inputMsg.length);
            char[] encodedMsg = encoder.genMessageAndRsCode(CharToHexRepr.toChar(inputMsg), nSym, polArithm);
            int actualErrs = generator.nextInt(nSym / 2);
            var corruptedMsg = messageCorrupter.corruptMessage(encodedMsg, actualErrs);
            var decodedMsg = decoder.decodeMessage(corruptedMsg, nSym, polArithm);
//            System.out.printf("nSym %s\n", nSym);
//            System.out.printf("Input %s\n", Arrays.toString(inputMsg));
//            System.out.printf("Encoded %s\n", Arrays.toString(CharToHexRepr.toInt(encodedMsg)));
//            System.out.printf("Corrupted %s\n", Arrays.toString(CharToHexRepr.toInt(corruptedMsg)));
//            System.out.printf("Decoded %s\n", Arrays.toString(CharToHexRepr.toInt(decodedMsg)));
            Assertions.assertArrayEquals(CharToHexRepr.toChar(inputMsg), decodedMsg);
            iterations--;
            if (iterations % 10 == 0) {
                System.out.println(iterations);
            }
        }
    }

    public int[] genRandomArray(int len, Random gen) {
        len = gen.nextInt(len) + 1;
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = gen.nextInt(255);
        }
        return arr;
    }
}
