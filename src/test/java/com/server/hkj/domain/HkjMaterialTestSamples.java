package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HkjMaterialTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HkjMaterial getHkjMaterialSample1() {
        return new HkjMaterial()
            .id(1L)
            .name("name1")
            .unit("unit1")
            .coverImage("coverImage1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static HkjMaterial getHkjMaterialSample2() {
        return new HkjMaterial()
            .id(2L)
            .name("name2")
            .unit("unit2")
            .coverImage("coverImage2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static HkjMaterial getHkjMaterialRandomSampleGenerator() {
        return new HkjMaterial()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .unit(UUID.randomUUID().toString())
            .coverImage(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
