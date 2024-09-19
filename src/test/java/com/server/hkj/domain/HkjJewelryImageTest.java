package com.server.hkj.domain;

import static com.server.hkj.domain.HkjJewelryImageTestSamples.*;
import static com.server.hkj.domain.HkjJewelryModelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjJewelryImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjJewelryImage.class);
        HkjJewelryImage hkjJewelryImage1 = getHkjJewelryImageSample1();
        HkjJewelryImage hkjJewelryImage2 = new HkjJewelryImage();
        assertThat(hkjJewelryImage1).isNotEqualTo(hkjJewelryImage2);

        hkjJewelryImage2.setId(hkjJewelryImage1.getId());
        assertThat(hkjJewelryImage1).isEqualTo(hkjJewelryImage2);

        hkjJewelryImage2 = getHkjJewelryImageSample2();
        assertThat(hkjJewelryImage1).isNotEqualTo(hkjJewelryImage2);
    }

    @Test
    void jewelryModelTest() {
        HkjJewelryImage hkjJewelryImage = getHkjJewelryImageRandomSampleGenerator();
        HkjJewelryModel hkjJewelryModelBack = getHkjJewelryModelRandomSampleGenerator();

        hkjJewelryImage.setJewelryModel(hkjJewelryModelBack);
        assertThat(hkjJewelryImage.getJewelryModel()).isEqualTo(hkjJewelryModelBack);

        hkjJewelryImage.jewelryModel(null);
        assertThat(hkjJewelryImage.getJewelryModel()).isNull();
    }
}
