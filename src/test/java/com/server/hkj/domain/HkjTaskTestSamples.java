package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class HkjTaskTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static HkjTask getHkjTaskSample1() {
        return new HkjTask()
            .id(1L)
            .name("name1")
            .coverImage("coverImage1")
            .description("description1")
            .point(1)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static HkjTask getHkjTaskSample2() {
        return new HkjTask()
            .id(2L)
            .name("name2")
            .coverImage("coverImage2")
            .description("description2")
            .point(2)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static HkjTask getHkjTaskRandomSampleGenerator() {
        return new HkjTask()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .coverImage(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .point(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
