package com.server.hkj.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class HkjSalaryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static HkjSalary getHkjSalarySample1() {
        return new HkjSalary().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static HkjSalary getHkjSalarySample2() {
        return new HkjSalary().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static HkjSalary getHkjSalaryRandomSampleGenerator() {
        return new HkjSalary()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
