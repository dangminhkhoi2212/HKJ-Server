package com.server.hkj.domain;

import static com.server.hkj.domain.HkjCartTestSamples.*;
import static com.server.hkj.domain.HkjJewelryModelTestSamples.*;
import static com.server.hkj.domain.UserExtraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjCartTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjCart.class);
        HkjCart hkjCart1 = getHkjCartSample1();
        HkjCart hkjCart2 = new HkjCart();
        assertThat(hkjCart1).isNotEqualTo(hkjCart2);

        hkjCart2.setId(hkjCart1.getId());
        assertThat(hkjCart1).isEqualTo(hkjCart2);

        hkjCart2 = getHkjCartSample2();
        assertThat(hkjCart1).isNotEqualTo(hkjCart2);
    }

    @Test
    void customerTest() {
        HkjCart hkjCart = getHkjCartRandomSampleGenerator();
        UserExtra userExtraBack = getUserExtraRandomSampleGenerator();

        hkjCart.setCustomer(userExtraBack);
        assertThat(hkjCart.getCustomer()).isEqualTo(userExtraBack);

        hkjCart.customer(null);
        assertThat(hkjCart.getCustomer()).isNull();
    }

    @Test
    void productTest() {
        HkjCart hkjCart = getHkjCartRandomSampleGenerator();
        HkjJewelryModel hkjJewelryModelBack = getHkjJewelryModelRandomSampleGenerator();

        hkjCart.setProduct(hkjJewelryModelBack);
        assertThat(hkjCart.getProduct()).isEqualTo(hkjJewelryModelBack);

        hkjCart.product(null);
        assertThat(hkjCart.getProduct()).isNull();
    }
}
