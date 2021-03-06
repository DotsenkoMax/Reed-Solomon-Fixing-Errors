package ru.msu.codes;

import ru.msu.codes.corrupter.MessageCorrupter;
import ru.msu.codes.corrupter.MessageCorrupterImpl;
import ru.msu.codes.decoder.ReedSolomonDecoder;
import ru.msu.codes.encoder.ReedSolomonEncoder;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    /**
     * Informative part
     */
    public static GaluaExample field = GaluaExample.ENUM_1;

    /**
     * Useless part
     */
    public static final Scanner enter = new Scanner(System.in);
    public static int galuaFieldDim = field.galuaFieldDim;
    public static int irreduciblePolynomial = field.irreduciblePolynomial;
    public static final int generatorNumber = field.generatorNumber;
    public static final Random generator = new Random(889);

    public static void main(String[] args) {
        GaluaFieldArithmetic gFLogic = new GaluaFieldArithmetic(galuaFieldDim, irreduciblePolynomial, generatorNumber);
        gFLogic.initAlphaTable();
        PolynomialArithmetic polynomialArithmetic = new PolynomialArithmetic(gFLogic);

        ReedSolomonEngine engine = new ReedSolomonEngineImpl(gFLogic);
        ReedSolomonEncoder encoder = engine.getEncoder();
        ReedSolomonDecoder decoder = engine.getDecoder();
        do {
            System.out.println("----------------------------------------------------");
            System.out.printf("Field: GF(%s), irrPolynomial: %s, Alpha: %s\n", galuaFieldDim, irreduciblePolynomial, generatorNumber);
            var msgLen = generator.nextInt(20) + 5;
            var messageIn = genArray(msgLen, galuaFieldDim);
            var nSym = generator.nextInt(msgLen) + 3;
            var nErrors = generator.nextInt(nSym / 2);
            MessageCorrupter corrupter = new MessageCorrupterImpl(msgLen + nSym);
            polynomialArithmetic.initGeneratorPolynomial(nSym);

            System.out.printf("MsgLen: %s. Error correcting symbols: %s. Errors: %s.\n", msgLen, nSym, nErrors);


            var encodedMessage = encoder.genMessageAndRsCode(messageIn, nSym, polynomialArithmetic);
            var corruptedMessage = corrupter.corruptMessage(encodedMessage, nErrors);
            var decodedMessage = decoder.decodeMessage(corruptedMessage, nSym, polynomialArithmetic);


            System.out.printf("InitialMessage: \n%s\n", Arrays.toString(messageIn));
            System.out.printf("EncodedMessage: \n%s\n", Arrays.toString(encodedMessage));
            System.out.printf("CorruptedMessage: \n%s\n", Arrays.toString(corruptedMessage));
            System.out.printf("DecodedMessage: \n%s\n", Arrays.toString(decodedMessage));
            System.out.printf("Equality: %s\n", Arrays.toString(messageIn).equals(Arrays.toString(decodedMessage)));
        } while (enter.nextLine() != null);

    }

    public static int[] genArray(int len, int maxValue) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = generator.nextInt(maxValue);
        }
        return arr;
    }
}