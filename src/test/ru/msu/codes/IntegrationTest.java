package ru.msu.codes;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.msu.codes.corrupter.MessageCorrupterImpl;

import java.util.Random;

public class IntegrationTest {

    @Test
    public void smallTest() {
        var gf = new GaluaFieldArithmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        var nSym = 2;
        var polArithm = new PolynomialArithmetic(gf);
        polArithm.initGeneratorPolynomial(nSym);
        var engine = new ReedSolomonEngineImpl(gf);
        var encoder = engine.getEncoder();
        var decoder = engine.getDecoder();
        int[] inputMsg = {2};
        int[] encoded = encoder.genMessageAndRsCode(inputMsg, nSym, polArithm);
        encoded[0] = 0;
        int[] decoded = decoder.decodeMessage(encoded, nSym, polArithm);
        Assertions.assertArrayEquals(inputMsg, decoded);
    }

    @Test
    @Ignore
    public void stressTest() {
        var gf = new GaluaFieldArithmetic(256, 0x11d, 0b10);
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
            int[] encodedMsg = encoder.genMessageAndRsCode(inputMsg, nSym, polArithm);
            int actualErrs = generator.nextInt(nSym / 2);
            var corruptedMsg = messageCorrupter.corruptMessage(encodedMsg, actualErrs);
            var decodedMsg = decoder.decodeMessage(corruptedMsg, nSym, polArithm);
            Assertions.assertArrayEquals(inputMsg, decodedMsg);
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
