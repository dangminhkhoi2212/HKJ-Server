package com.server.hkj.domain;

import static com.server.hkj.domain.HkjCategoryTestSamples.*;
import static com.server.hkj.domain.HkjJewelryModelTestSamples.*;
import static com.server.hkj.domain.HkjMaterialTestSamples.*;
import static com.server.hkj.domain.HkjOrderImageTestSamples.*;
import static com.server.hkj.domain.HkjOrderTestSamples.*;
import static com.server.hkj.domain.HkjProjectTestSamples.*;
import static com.server.hkj.domain.UserExtraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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
    void orderImagesTest() {
        HkjOrder hkjOrder = getHkjOrderRandomSampleGenerator();
        HkjOrderImage hkjOrderImageBack = getHkjOrderImageRandomSampleGenerator();

        hkjOrder.addOrderImages(hkjOrderImageBack);
        assertThat(hkjOrder.getOrderImages()).containsOnly(hkjOrderImageBack);
        assertThat(hkjOrderImageBack.getOrder()).isEqualTo(hkjOrder);

        hkjOrder.removeOrderImages(hkjOrderImageBack);
        assertThat(hkjOrder.getOrderImages()).doesNotContain(hkjOrderImageBack);
        assertThat(hkjOrderImageBack.getOrder()).isNull();

        hkjOrder.orderImages(new HashSet<>(Set.of(hkjOrderImageBack)));
        assertThat(hkjOrder.getOrderImages()).containsOnly(hkjOrderImageBack);
        assertThat(hkjOrderImageBack.getOrder()).isEqualTo(hkjOrder);

        hkjOrder.setOrderImages(new HashSet<>());
        assertThat(hkjOrder.getOrderImages()).doesNotContain(hkjOrderImageBack);
        assertThat(hkjOrderImageBack.getOrder()).isNull();
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
    void materialTest() {
        HkjOrder hkjOrder = getHkjOrderRandomSampleGenerator();
        HkjMaterial hkjMaterialBack = getHkjMaterialRandomSampleGenerator();

        hkjOrder.setMaterial(hkjMaterialBack);
        assertThat(hkjOrder.getMaterial()).isEqualTo(hkjMaterialBack);

        hkjOrder.material(null);
        assertThat(hkjOrder.getMaterial()).isNull();
    }

    @Test
    void jewelryTest() {
        HkjOrder hkjOrder = getHkjOrderRandomSampleGenerator();
        HkjJewelryModel hkjJewelryModelBack = getHkjJewelryModelRandomSampleGenerator();

        hkjOrder.setJewelry(hkjJewelryModelBack);
        assertThat(hkjOrder.getJewelry()).isEqualTo(hkjJewelryModelBack);

        hkjOrder.jewelry(null);
        assertThat(hkjOrder.getJewelry()).isNull();
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

    @Test
    void categoryTest() {
        HkjOrder hkjOrder = getHkjOrderRandomSampleGenerator();
        HkjCategory hkjCategoryBack = getHkjCategoryRandomSampleGenerator();

        hkjOrder.setCategory(hkjCategoryBack);
        assertThat(hkjOrder.getCategory()).isEqualTo(hkjCategoryBack);

        hkjOrder.category(null);
        assertThat(hkjOrder.getCategory()).isNull();
    }
}
