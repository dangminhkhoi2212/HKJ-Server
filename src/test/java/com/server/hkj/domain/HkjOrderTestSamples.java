package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class HkjOrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static HkjOrder getHkjOrderSample1() {
        return new HkjOrder()
            .id(1L)
            .specialRequests("specialRequests1")
            .customerRating(1)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static HkjOrder getHkjOrderSample2() {
        return new HkjOrder()
            .id(2L)
            .specialRequests("specialRequests2")
            .customerRating(2)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static HkjOrder getHkjOrderRandomSampleGenerator() {
        return new HkjOrder()
            .id(longCount.incrementAndGet())
            .specialRequests(UUID.randomUUID().toString())
            .customerRating(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
