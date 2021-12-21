package ru.msu.codes;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;

public class PolynomialTest {
    @Test
    public void test() {
        var gf = new GaluaFieldArithmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        Polynomial divisor = new Polynomial(new char[]{0, 0, 0, 0, 0x56, 0x34, 0x12}, gf);
        Polynomial divider = new Polynomial(new char[]{0x40, 0x78, 0x36, 0x0f, 0x01}, gf);

        Assertions.assertArrayEquals(new char[]{0xd9, 0x78, 0xe6, 0x37}, divisor.divide(divider).toCharArray());
    }
    @Test
    public void test2() {
        var gf = new GaluaFieldArithmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        Polynomial divisor = new Polynomial(new char[]{67, 86, 136, 68}, gf);
        Polynomial divider = new Polynomial(new char[]{6, 11, 7}, gf);
        System.out.println(Arrays.toString(divisor.divide(divider).toIntArray()));
        Assertions.assertArrayEquals(new char[]{9, 87}, divisor.divide(divider).toCharArray());
    }

    @Test
    public void multTest() {
        var gf = new GaluaFieldArithmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        Polynomial divisor = new Polynomial(new char[]{8, 9, 2}, gf);
        Polynomial divider = new Polynomial(new char[]{6, 11}, gf);
        Assertions.assertArrayEquals(new char[]{48, 110, 95, 22}, divisor.mult(divider).toCharArray());
    }
}
