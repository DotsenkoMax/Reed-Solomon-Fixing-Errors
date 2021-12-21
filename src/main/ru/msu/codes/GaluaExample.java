package ru.msu.codes;

enum GaluaExample {
    ENUM_1(1 << 8, 0x11d, 0b10), // x^8 + x^4 + x^3 + x^2 + 1
    ENUM_2(1 << 16, 69643, 0b10), // x^16 + x^12 + x^3 + x + 1
    ENUM_3(1 << 8, 0x11b, 0b11); // x^8 + x^4 + x^3 + x + 1

    public int galuaFieldDim;
    public int irreduciblePolynomial;
    public int generatorNumber;

    GaluaExample(int galuaFieldDim, int irreduciblePolynomial, int generatorNumber) {
        this.galuaFieldDim = galuaFieldDim;
        this.irreduciblePolynomial = irreduciblePolynomial;
        this.generatorNumber = generatorNumber;
    }
}
