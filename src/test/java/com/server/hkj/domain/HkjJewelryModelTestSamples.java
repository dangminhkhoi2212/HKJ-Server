package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class HkjJewelryModelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static HkjJewelryModel getHkjJewelryModelSample1() {
        return new HkjJewelryModel()
            .id(1L)
            .sku("sku1")
            .name("name1")
            .description("description1")
            .coverImage("coverImage1")
            .daysCompleted(1)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static HkjJewelryModel getHkjJewelryModelSample2() {
        return new HkjJewelryModel()
            .id(2L)
            .sku("sku2")
            .name("name2")
            .description("description2")
            .coverImage("coverImage2")
            .daysCompleted(2)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static HkjJewelryModel getHkjJewelryModelRandomSampleGenerator() {
        return new HkjJewelryModel()
            .id(longCount.incrementAndGet())
            .sku(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .coverImage(UUID.randomUUID().toString())
            .daysCompleted(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
