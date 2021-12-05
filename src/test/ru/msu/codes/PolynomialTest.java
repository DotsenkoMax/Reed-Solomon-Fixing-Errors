package ru.msu.codes;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;

public class PolynomialTest {
    @Test
    public void test() {
        var gf = new GaluaFieldAriphmetic(256, 0x11d, 0b10);
        gf.initAlphaTable();
        Polynomial divisor = new Polynomial(new char[]{0x12, 0x34, 0x56, 0, 0, 0, 0}, gf);
        Polynomial divider = new Polynomial(new char[]{0x01, 0x0f, 0x36, 0x78, 0x40}, gf);

        Assertions.assertArrayEquals(new char[]{0x37, 0xe6, 0x78, 0xd9}, divisor.divide(divider).toCharArray());
    }
}
