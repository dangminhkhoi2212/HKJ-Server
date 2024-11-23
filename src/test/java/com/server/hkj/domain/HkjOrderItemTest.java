package com.server.hkj.domain;

import static com.server.hkj.domain.HkjCategoryTestSamples.*;
import static com.server.hkj.domain.HkjJewelryModelTestSamples.*;
import static com.server.hkj.domain.HkjMaterialTestSamples.*;
import static com.server.hkj.domain.HkjOrderItemTestSamples.*;
import static com.server.hkj.domain.HkjOrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjOrderItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjOrderItem.class);
        HkjOrderItem hkjOrderItem1 = getHkjOrderItemSample1();
        HkjOrderItem hkjOrderItem2 = new HkjOrderItem();
        assertThat(hkjOrderItem1).isNotEqualTo(hkjOrderItem2);

        hkjOrderItem2.setId(hkjOrderItem1.getId());
        assertThat(hkjOrderItem1).isEqualTo(hkjOrderItem2);

        hkjOrderItem2 = getHkjOrderItemSample2();
        assertThat(hkjOrderItem1).isNotEqualTo(hkjOrderItem2);
    }

    @Test
    void materialTest() {
        HkjOrderItem hkjOrderItem = getHkjOrderItemRandomSampleGenerator();
        HkjMaterial hkjMaterialBack = getHkjMaterialRandomSampleGenerator();

        hkjOrderItem.setMaterial(hkjMaterialBack);
        assertThat(hkjOrderItem.getMaterial()).isEqualTo(hkjMaterialBack);

        hkjOrderItem.material(null);
        assertThat(hkjOrderItem.getMaterial()).isNull();
    }

    @Test
    void orderTest() {
        HkjOrderItem hkjOrderItem = getHkjOrderItemRandomSampleGenerator();
        HkjOrder hkjOrderBack = getHkjOrderRandomSampleGenerator();

        hkjOrderItem.setOrder(hkjOrderBack);
        assertThat(hkjOrderItem.getOrder()).isEqualTo(hkjOrderBack);

        hkjOrderItem.order(null);
        assertThat(hkjOrderItem.getOrder()).isNull();
    }

    @Test
    void productTest() {
        HkjOrderItem hkjOrderItem = getHkjOrderItemRandomSampleGenerator();
        HkjJewelryModel hkjJewelryModelBack = getHkjJewelryModelRandomSampleGenerator();

        hkjOrderItem.setProduct(hkjJewelryModelBack);
        assertThat(hkjOrderItem.getProduct()).isEqualTo(hkjJewelryModelBack);

        hkjOrderItem.product(null);
        assertThat(hkjOrderItem.getProduct()).isNull();
    }

    @Test
    void categoryTest() {
        HkjOrderItem hkjOrderItem = getHkjOrderItemRandomSampleGenerator();
        HkjCategory hkjCategoryBack = getHkjCategoryRandomSampleGenerator();

        hkjOrderItem.setCategory(hkjCategoryBack);
        assertThat(hkjOrderItem.getCategory()).isEqualTo(hkjCategoryBack);

        hkjOrderItem.category(null);
        assertThat(hkjOrderItem.getCategory()).isNull();
    }
}
