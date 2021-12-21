package ru.msu.codes;

import ru.msu.codes.decoder.ReedSolomonDecoder;
import ru.msu.codes.encoder.ReedSolomonEncoder;


/**
 * Генератор инкодера и декодера
 */
public interface ReedSolomonEngine {
    ReedSolomonDecoder getDecoder();

    ReedSolomonEncoder getEncoder();
}
