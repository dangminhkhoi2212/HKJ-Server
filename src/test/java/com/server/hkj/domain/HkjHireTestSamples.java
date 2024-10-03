package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HkjHireTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HkjHire getHkjHireSample1() {
        return new HkjHire().id(1L).notes("notes1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static HkjHire getHkjHireSample2() {
        return new HkjHire().id(2L).notes("notes2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static HkjHire getHkjHireRandomSampleGenerator() {
        return new HkjHire()
            .id(longCount.incrementAndGet())
            .notes(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
