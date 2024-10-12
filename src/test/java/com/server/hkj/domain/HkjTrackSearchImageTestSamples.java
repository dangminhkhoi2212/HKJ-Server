package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class HkjTrackSearchImageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static HkjTrackSearchImage getHkjTrackSearchImageSample1() {
        return new HkjTrackSearchImage().id(1L).searchOrder(1).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static HkjTrackSearchImage getHkjTrackSearchImageSample2() {
        return new HkjTrackSearchImage().id(2L).searchOrder(2).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static HkjTrackSearchImage getHkjTrackSearchImageRandomSampleGenerator() {
        return new HkjTrackSearchImage()
            .id(longCount.incrementAndGet())
            .searchOrder(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
