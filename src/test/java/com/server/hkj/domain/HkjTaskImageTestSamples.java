package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HkjTaskImageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HkjTaskImage getHkjTaskImageSample1() {
        return new HkjTaskImage().id(1L).url("url1").description("description1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static HkjTaskImage getHkjTaskImageSample2() {
        return new HkjTaskImage().id(2L).url("url2").description("description2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static HkjTaskImage getHkjTaskImageRandomSampleGenerator() {
        return new HkjTaskImage()
            .id(longCount.incrementAndGet())
            .url(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
