package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class HkjCartTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static HkjCart getHkjCartSample1() {
        return new HkjCart().id(1L).quantity(1).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static HkjCart getHkjCartSample2() {
        return new HkjCart().id(2L).quantity(2).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static HkjCart getHkjCartRandomSampleGenerator() {
        return new HkjCart()
            .id(longCount.incrementAndGet())
            .quantity(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
