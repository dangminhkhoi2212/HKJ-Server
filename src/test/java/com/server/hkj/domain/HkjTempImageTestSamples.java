package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HkjTempImageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HkjTempImage getHkjTempImageSample1() {
        return new HkjTempImage().id(1L).url("url1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static HkjTempImage getHkjTempImageSample2() {
        return new HkjTempImage().id(2L).url("url2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static HkjTempImage getHkjTempImageRandomSampleGenerator() {
        return new HkjTempImage()
            .id(longCount.incrementAndGet())
            .url(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
