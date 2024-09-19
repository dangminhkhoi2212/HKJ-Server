package com.server.hkj.domain;

import static com.server.hkj.domain.HkjOrderImageTestSamples.*;
import static com.server.hkj.domain.HkjOrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjOrderImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjOrderImage.class);
        HkjOrderImage hkjOrderImage1 = getHkjOrderImageSample1();
        HkjOrderImage hkjOrderImage2 = new HkjOrderImage();
        assertThat(hkjOrderImage1).isNotEqualTo(hkjOrderImage2);

        hkjOrderImage2.setId(hkjOrderImage1.getId());
        assertThat(hkjOrderImage1).isEqualTo(hkjOrderImage2);

        hkjOrderImage2 = getHkjOrderImageSample2();
        assertThat(hkjOrderImage1).isNotEqualTo(hkjOrderImage2);
    }

    @Test
    void hkjOrderTest() {
        HkjOrderImage hkjOrderImage = getHkjOrderImageRandomSampleGenerator();
        HkjOrder hkjOrderBack = getHkjOrderRandomSampleGenerator();

        hkjOrderImage.setHkjOrder(hkjOrderBack);
        assertThat(hkjOrderImage.getHkjOrder()).isEqualTo(hkjOrderBack);

        hkjOrderImage.hkjOrder(null);
        assertThat(hkjOrderImage.getHkjOrder()).isNull();
    }
}