package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class HkjOrderItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static HkjOrderItem getHkjOrderItemSample1() {
        return new HkjOrderItem()
            .id(1L)
            .quantity(1)
            .specialRequests("specialRequests1")
            .notes("notes1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static HkjOrderItem getHkjOrderItemSample2() {
        return new HkjOrderItem()
            .id(2L)
            .quantity(2)
            .specialRequests("specialRequests2")
            .notes("notes2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static HkjOrderItem getHkjOrderItemRandomSampleGenerator() {
        return new HkjOrderItem()
            .id(longCount.incrementAndGet())
            .quantity(intCount.incrementAndGet())
            .specialRequests(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
