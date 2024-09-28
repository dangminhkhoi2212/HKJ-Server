package com.server.hkj.domain;

import static com.server.hkj.domain.HkjMaterialImageTestSamples.*;
import static com.server.hkj.domain.HkjMaterialTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjMaterialImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjMaterialImage.class);
        HkjMaterialImage hkjMaterialImage1 = getHkjMaterialImageSample1();
        HkjMaterialImage hkjMaterialImage2 = new HkjMaterialImage();
        assertThat(hkjMaterialImage1).isNotEqualTo(hkjMaterialImage2);

        hkjMaterialImage2.setId(hkjMaterialImage1.getId());
        assertThat(hkjMaterialImage1).isEqualTo(hkjMaterialImage2);

        hkjMaterialImage2 = getHkjMaterialImageSample2();
        assertThat(hkjMaterialImage1).isNotEqualTo(hkjMaterialImage2);
    }

    @Test
    void hkjMaterialTest() {
        HkjMaterialImage hkjMaterialImage = getHkjMaterialImageRandomSampleGenerator();
        HkjMaterial hkjMaterialBack = getHkjMaterialRandomSampleGenerator();

        hkjMaterialImage.setHkjMaterial(hkjMaterialBack);
        assertThat(hkjMaterialImage.getHkjMaterial()).isEqualTo(hkjMaterialBack);

        hkjMaterialImage.hkjMaterial(null);
        assertThat(hkjMaterialImage.getHkjMaterial()).isNull();
    }
}
