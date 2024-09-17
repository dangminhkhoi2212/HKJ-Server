package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HkjHireTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HkjHire getHkjHireSample1() {
        return new HkjHire().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static HkjHire getHkjHireSample2() {
        return new HkjHire().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static HkjHire getHkjHireRandomSampleGenerator() {
        return new HkjHire()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
