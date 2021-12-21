package ru.msu.codes;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

public class GaluaFieldArithmeticTest {

    GaluaFieldArithmetic gf = new GaluaFieldArithmetic(256, 0x11d, 0b10);

    @BeforeEach
    public void init() {
    }

    @Test
    public void test() {
        gf.initAlphaTable();
        Assertions.assertEquals(gf.degreeIdx2GFValue[0], 0b1);
        Assertions.assertEquals(gf.degreeIdx2GFValue[1], 0b10);
        Assertions.assertEquals(gf.degreeIdx2GFValue[2], 0b00000100);
        Assertions.assertEquals(gf.degreeIdx2GFValue[3], 0b00001000);
        Assertions.assertEquals(gf.degreeIdx2GFValue[4], 0b00010000);
        Assertions.assertEquals(gf.degreeIdx2GFValue[5], 0b00100000);
        Assertions.assertEquals(gf.degreeIdx2GFValue[6], 0b01000000);
        Assertions.assertEquals(gf.degreeIdx2GFValue[7], 0b10000000);
        Assertions.assertEquals(gf.degreeIdx2GFValue[8], 0b00011101);
        Assertions.assertEquals(gf.degreeIdx2GFValue[9], 0b00111010);
        Assertions.assertEquals(gf.degreeIdx2GFValue[10], 0b01110100);
        Assertions.assertEquals(gf.degreeIdx2GFValue[11], 0b11101000);
        Assertions.assertEquals(gf.degreeIdx2GFValue[12], 0b11001101);
        Assertions.assertEquals(gf.degreeIdx2GFValue[13], 0b10000111);
        Assertions.assertEquals(gf.degreeIdx2GFValue[14], 0b00010011);
        Assertions.assertEquals(gf.degreeIdx2GFValue[15], 0b00100110);
        Assertions.assertEquals(gf.degreeIdx2GFValue[0], gf.degreeIdx2GFValue[255]);
        Assertions.assertEquals(gf.multiplyRussianPeasantMultiplicationAlgo(0b10001001, 0b00101010), 0b11000011);
    }

    @Test
    public void sumTest() {
        gf.initAlphaTable();
        var first = gf.sum(gf.degreeIdx2GFValue[0], gf.degreeIdx2GFValue[1]);
        var second = gf.sum(gf.degreeIdx2GFValue[2], gf.degreeIdx2GFValue[3]);
        Assertions.assertEquals(0x0f, gf.sum(first, second));
    }
}
