package com.server.hkj.domain;

import static com.server.hkj.domain.HkjCartTestSamples.*;
import static com.server.hkj.domain.HkjJewelryModelTestSamples.*;
import static com.server.hkj.domain.UserExtraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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
    void productTest() {
        HkjCart hkjCart = getHkjCartRandomSampleGenerator();
        HkjJewelryModel hkjJewelryModelBack = getHkjJewelryModelRandomSampleGenerator();

        hkjCart.addProduct(hkjJewelryModelBack);
        assertThat(hkjCart.getProducts()).containsOnly(hkjJewelryModelBack);
        assertThat(hkjJewelryModelBack.getHkjCart()).isEqualTo(hkjCart);

        hkjCart.removeProduct(hkjJewelryModelBack);
        assertThat(hkjCart.getProducts()).doesNotContain(hkjJewelryModelBack);
        assertThat(hkjJewelryModelBack.getHkjCart()).isNull();

        hkjCart.products(new HashSet<>(Set.of(hkjJewelryModelBack)));
        assertThat(hkjCart.getProducts()).containsOnly(hkjJewelryModelBack);
        assertThat(hkjJewelryModelBack.getHkjCart()).isEqualTo(hkjCart);

        hkjCart.setProducts(new HashSet<>());
        assertThat(hkjCart.getProducts()).doesNotContain(hkjJewelryModelBack);
        assertThat(hkjJewelryModelBack.getHkjCart()).isNull();
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
}
