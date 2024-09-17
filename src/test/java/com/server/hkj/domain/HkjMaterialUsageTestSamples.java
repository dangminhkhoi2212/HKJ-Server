package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class HkjMaterialUsageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static HkjMaterialUsage getHkjMaterialUsageSample1() {
        return new HkjMaterialUsage()
            .id(1L)
            .quantity(1)
            .lossQuantity(1)
            .notes("notes1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static HkjMaterialUsage getHkjMaterialUsageSample2() {
        return new HkjMaterialUsage()
            .id(2L)
            .quantity(2)
            .lossQuantity(2)
            .notes("notes2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static HkjMaterialUsage getHkjMaterialUsageRandomSampleGenerator() {
        return new HkjMaterialUsage()
            .id(longCount.incrementAndGet())
            .quantity(intCount.incrementAndGet())
            .lossQuantity(intCount.incrementAndGet())
            .notes(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
