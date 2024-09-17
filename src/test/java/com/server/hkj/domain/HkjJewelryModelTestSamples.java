package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HkjJewelryModelTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HkjJewelryModel getHkjJewelryModelSample1() {
        return new HkjJewelryModel()
            .id(1L)
            .name("name1")
            .description("description1")
            .color("color1")
            .notes("notes1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static HkjJewelryModel getHkjJewelryModelSample2() {
        return new HkjJewelryModel()
            .id(2L)
            .name("name2")
            .description("description2")
            .color("color2")
            .notes("notes2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static HkjJewelryModel getHkjJewelryModelRandomSampleGenerator() {
        return new HkjJewelryModel()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .color(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
