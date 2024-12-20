package com.server.hkj.domain;

import static com.server.hkj.domain.HkjOrderTestSamples.*;
import static com.server.hkj.domain.HkjProjectTestSamples.*;
import static com.server.hkj.domain.UserExtraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjOrder.class);
        HkjOrder hkjOrder1 = getHkjOrderSample1();
        HkjOrder hkjOrder2 = new HkjOrder();
        assertThat(hkjOrder1).isNotEqualTo(hkjOrder2);

        hkjOrder2.setId(hkjOrder1.getId());
        assertThat(hkjOrder1).isEqualTo(hkjOrder2);

        hkjOrder2 = getHkjOrderSample2();
        assertThat(hkjOrder1).isNotEqualTo(hkjOrder2);
    }

    @Test
    void customerTest() {
        HkjOrder hkjOrder = getHkjOrderRandomSampleGenerator();
        UserExtra userExtraBack = getUserExtraRandomSampleGenerator();

        hkjOrder.setCustomer(userExtraBack);
        assertThat(hkjOrder.getCustomer()).isEqualTo(userExtraBack);

        hkjOrder.customer(null);
        assertThat(hkjOrder.getCustomer()).isNull();
    }

    @Test
    void projectTest() {
        HkjOrder hkjOrder = getHkjOrderRandomSampleGenerator();
        HkjProject hkjProjectBack = getHkjProjectRandomSampleGenerator();

        hkjOrder.setProject(hkjProjectBack);
        assertThat(hkjOrder.getProject()).isEqualTo(hkjProjectBack);

        hkjOrder.project(null);
        assertThat(hkjOrder.getProject()).isNull();
    }
}
