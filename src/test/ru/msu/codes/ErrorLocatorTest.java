package ru.msu.codes;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.msu.codes.decoder.ErrorLocator;
import ru.msu.codes.decoder.SyndromesPolynomialCalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ErrorLocatorTest {
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
        encodedMsg[0] = 6;
        encodedMsg[10] = 7;
        encodedMsg[20] = 8;
        var syndCalc = new SyndromesPolynomialCalculator(gf);
        var errCalc = new ErrorLocator(gf);
        var synd = syndCalc.calcSyndromes(new Polynomial(encodedMsg, gf), nSym);
        var errLoc = errCalc.findErrorLocator(synd, nSym);
        Assertions.assertArrayEquals(new Integer[]{0, 10, 20}, errCalc.findPositions(errLoc, encodedMsg.length).toArray(new Integer[3]));
    }

    @Test
    public void oneTest() {
        /*
            Input message: [36, 106, 109, 191]
            Encoded message: [36, 106, 109, 191, 142, 25, 11]
            nSym 3
            actualErrs 2
            Corrupted message: [36, 106, 108, 190, 142, 25, 11]
            Input lenght: 4 Actual errs: 2. Places: [2, 3]
            errsFound: 0, totalErrs: 2
         */
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        var encoder = new ReedSolomonEncoderImpl(gf);
        int nSym = 3;
        var polArithm = new PolynomialArithmetic(gf);
        polArithm.initGeneratorPolynomial(nSym);
        var encodedMsg = encoder.genMessageAndRsCode(new char[]{36, 106, 109, 191}, nSym, polArithm);

        encodedMsg[2] = 108;
        encodedMsg[3] = 190;
        var syndCalc = new SyndromesPolynomialCalculator(gf);
        var errCalc = new ErrorLocator(gf);
        var synd = syndCalc.calcSyndromes(new Polynomial(encodedMsg, gf), nSym);
        var errLoc = errCalc.findErrorLocator(synd, nSym);
        Assertions.assertArrayEquals(new Integer[]{2, 3}, errCalc.findPositions(errLoc, encodedMsg.length).toArray(new Integer[2]));
    }

    @Test
    public void stressTest() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        var polArithm = new PolynomialArithmetic(gf);
        var engine = new ReedSolomonEngineImpl(gf);
        var syndrome = new SyndromesPolynomialCalculator(gf);
        var errLoc = new ErrorLocator(gf);
        var encoder = engine.getEncoder();
        int iterations = 1000;
        var generator = new Random();
        while (iterations != 0) {
            int[] inputMsg = genRandomArray(30, generator);
            int nSym = (generator.nextInt(inputMsg.length) + 1) * 2;
            polArithm.initGeneratorPolynomial(nSym);
            char[] encodedMsg = encoder.genMessageAndRsCode(CharToHexRepr.toChar(inputMsg), nSym, polArithm);

            int actualErrs = generator.nextInt(nSym / 2);
            var actualPositions = IntStream.range(0, inputMsg.length).boxed().collect(Collectors.toList());
            Collections.shuffle(actualPositions);
            var actualPrefix = new ArrayList<Integer>();

            int cnt = 0;
            System.out.printf("Input message: %s\n", Arrays.toString(inputMsg));
            System.out.printf("Encoded message: %s\n", Arrays.toString(CharToHexRepr.toInt(encodedMsg)));
            System.out.printf("nSym %s\n", nSym);
            System.out.printf("actualErrs %s\n", actualErrs);
            for (var idx : actualPositions) {
                if (cnt == actualErrs) {
                    break;
                }
                actualPrefix.add(idx);
                encodedMsg[idx] = (char) gf.sum(encodedMsg[idx], 1);
                cnt += 1;
            }
            actualPrefix.sort(Integer::compareTo);
            System.out.printf("Corrupted message: %s\n", Arrays.toString(CharToHexRepr.toInt(encodedMsg)));
            System.out.printf("Input lenght: %s Actual errs: %s. Places: %s\n", inputMsg.length, actualErrs, actualPrefix);
            Polynomial synd = syndrome.calcSyndromes(new Polynomial(encodedMsg, gf), nSym);
            Polynomial errors = errLoc.findErrorLocator(synd, nSym);
            var positions = errLoc.findPositions(errors, encodedMsg.length);
            Assertions.assertArrayEquals(actualPrefix.stream().sorted().toArray(), positions.stream().sorted().toArray());
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
