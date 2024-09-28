package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HkjMaterialImageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HkjMaterialImage getHkjMaterialImageSample1() {
        return new HkjMaterialImage().id(1L).url("url1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static HkjMaterialImage getHkjMaterialImageSample2() {
        return new HkjMaterialImage().id(2L).url("url2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static HkjMaterialImage getHkjMaterialImageRandomSampleGenerator() {
        return new HkjMaterialImage()
            .id(longCount.incrementAndGet())
            .url(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
