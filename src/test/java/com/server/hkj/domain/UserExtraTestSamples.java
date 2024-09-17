package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserExtraTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserExtra getUserExtraSample1() {
        return new UserExtra().id(1L).phone("phone1").address("address1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static UserExtra getUserExtraSample2() {
        return new UserExtra().id(2L).phone("phone2").address("address2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static UserExtra getUserExtraRandomSampleGenerator() {
        return new UserExtra()
            .id(longCount.incrementAndGet())
            .phone(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
