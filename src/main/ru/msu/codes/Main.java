package ru.msu.codes;

import ru.msu.codes.decoder.ReedSolomonDecoder;

import java.util.Scanner;

import static ru.msu.codes.CharToHexRepr.toHex;

public class Main {
    public static final Scanner enter = new Scanner(System.in);
    public static final int galuaFieldDim = 1 << 8; // 256 = GF(2^8)
    public static final int irreduciblePolynomial = 0b100011101; // 0x11d or x^8 + x^4 + x^3 + x^2 + 1
    public static final int generatorNumber = 0b10; // alpha or x

    //    @SuppressWarnings("all")
    public static void main(String[] args) {
        GaluaFieldAriphmetic gFLogic = new GaluaFieldAriphmetic(galuaFieldDim, irreduciblePolynomial, generatorNumber);
        gFLogic.initAlphaTable();
        PolynomialArithmetic polynomialArithmetic = new PolynomialArithmetic(gFLogic);

        ReedSolomonEngine engine = new ReedSolomonEngineImpl(gFLogic);
        ReedSolomonEncoder encoder = engine.getEncoder();
        ReedSolomonDecoder decoder = engine.getDecoder();
        while (true) {
            System.out.println("<MessageIn: String> <nSym: int> <nErrorsAppend: int>");
            var messageIn = enter.next().toCharArray();
            var nSym = enter.nextInt();
            var nErrorsAppend = enter.nextInt();
            assert nSym + messageIn.length < galuaFieldDim;
            assert nErrorsAppend <= nSym;

            MessageCorrupter corrupter = new MessageCorrupterImpl(messageIn.length);
            polynomialArithmetic.initGeneratorPolynomial(nSym);

            System.out.printf("InitialMessage: \n%s\n", (Object) toHex(messageIn));
            var encodedMessage = encoder.genMessageAndRsCode(messageIn, nSym, polynomialArithmetic);
            System.out.printf("EncodedMessage: \n%s\n", (Object) toHex(encodedMessage));
            var corruptedMessage = corrupter.corruptMessage(encodedMessage, nErrorsAppend);
            System.out.printf("CorruptedMessage: \n%s\n", (Object) toHex(corruptedMessage));
            var decodedMessage = decoder.decodeMessage(corruptedMessage, nSym, polynomialArithmetic);

        }
    }
}