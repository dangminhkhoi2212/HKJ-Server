package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HkjProjectTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HkjProject getHkjProjectSample1() {
        return new HkjProject()
            .id(1L)
            .name("name1")
            .coverImage("coverImage1")
            .description("description1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static HkjProject getHkjProjectSample2() {
        return new HkjProject()
            .id(2L)
            .name("name2")
            .coverImage("coverImage2")
            .description("description2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static HkjProject getHkjProjectRandomSampleGenerator() {
        return new HkjProject()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .coverImage(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
