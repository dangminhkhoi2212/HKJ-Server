package com.server.hkj.domain;

import java.util.UUID;

public class ApplicationUserTestSamples {

    public static ApplicationUser getApplicationUserSample1() {
        return new ApplicationUser().id("id1").phoneNumber("phoneNumber1");
    }

    public static ApplicationUser getApplicationUserSample2() {
        return new ApplicationUser().id("id2").phoneNumber("phoneNumber2");
    }

    public static ApplicationUser getApplicationUserRandomSampleGenerator() {
        return new ApplicationUser().id(UUID.randomUUID().toString()).phoneNumber(UUID.randomUUID().toString());
    }
}
