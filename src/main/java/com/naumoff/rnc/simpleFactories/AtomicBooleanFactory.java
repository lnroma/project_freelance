package com.naumoff.rnc.simpleFactories;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class AtomicBooleanFactory {
    public AtomicBoolean createAtomicBoolean() {
        return new AtomicBoolean();
    }

    public AtomicBoolean createAtomicBoolean(Boolean value) {
        return new AtomicBoolean(value);
    }
}
