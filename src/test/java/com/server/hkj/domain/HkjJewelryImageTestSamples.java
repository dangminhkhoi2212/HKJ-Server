package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HkjJewelryImageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HkjJewelryImage getHkjJewelryImageSample1() {
        return new HkjJewelryImage()
            .id(1L)
            .url("url1")
            .description("description1")
            .tags("tags1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static HkjJewelryImage getHkjJewelryImageSample2() {
        return new HkjJewelryImage()
            .id(2L)
            .url("url2")
            .description("description2")
            .tags("tags2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static HkjJewelryImage getHkjJewelryImageRandomSampleGenerator() {
        return new HkjJewelryImage()
            .id(longCount.incrementAndGet())
            .url(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .tags(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
