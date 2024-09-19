package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HkjOrderImageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HkjOrderImage getHkjOrderImageSample1() {
        return new HkjOrderImage().id(1L).url("url1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static HkjOrderImage getHkjOrderImageSample2() {
        return new HkjOrderImage().id(2L).url("url2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static HkjOrderImage getHkjOrderImageRandomSampleGenerator() {
        return new HkjOrderImage()
            .id(longCount.incrementAndGet())
            .url(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
