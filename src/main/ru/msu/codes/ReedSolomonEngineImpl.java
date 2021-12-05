package ru.msu.codes;

import ru.msu.codes.decoder.ReedSolomonDecoder;
import ru.msu.codes.decoder.ReedSolomonDecoderImpl;

public class ReedSolomonEngineImpl implements ReedSolomonEngine {
    private final GaluaFieldAriphmetic gFLogic;

    public ReedSolomonEngineImpl(GaluaFieldAriphmetic gFLogic) {
        this.gFLogic = gFLogic;
    }

    @Override
    public ReedSolomonDecoder getDecoder() {
        return new ReedSolomonDecoderImpl(gFLogic);
    }

    @Override
    public ReedSolomonEncoder getEncoder() {
        return new ReedSolomonEncoderImpl(gFLogic);
    }
}
