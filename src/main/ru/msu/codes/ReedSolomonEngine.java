package ru.msu.codes;

import ru.msu.codes.decoder.ReedSolomonDecoder;

public interface ReedSolomonEngine {
    ReedSolomonDecoder getDecoder();

    ReedSolomonEncoder getEncoder();
}
