package ru.msu.codes;

import ru.msu.codes.decoder.ReedSolomonDecoder;
import ru.msu.codes.encoder.ReedSolomonEncoder;

public interface ReedSolomonEngine {
    ReedSolomonDecoder getDecoder();

    ReedSolomonEncoder getEncoder();
}
