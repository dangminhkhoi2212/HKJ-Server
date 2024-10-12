package com.server.hkj.domain;

import static com.server.hkj.domain.HkjMaterialImageTestSamples.*;
import static com.server.hkj.domain.HkjMaterialTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HkjMaterialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjMaterial.class);
        HkjMaterial hkjMaterial1 = getHkjMaterialSample1();
        HkjMaterial hkjMaterial2 = new HkjMaterial();
        assertThat(hkjMaterial1).isNotEqualTo(hkjMaterial2);

        hkjMaterial2.setId(hkjMaterial1.getId());
        assertThat(hkjMaterial1).isEqualTo(hkjMaterial2);

        hkjMaterial2 = getHkjMaterialSample2();
        assertThat(hkjMaterial1).isNotEqualTo(hkjMaterial2);
    }

    @Test
    void imagesTest() {
        HkjMaterial hkjMaterial = getHkjMaterialRandomSampleGenerator();
        HkjMaterialImage hkjMaterialImageBack = getHkjMaterialImageRandomSampleGenerator();

        hkjMaterial.addImages(hkjMaterialImageBack);
        assertThat(hkjMaterial.getImages()).containsOnly(hkjMaterialImageBack);
        assertThat(hkjMaterialImageBack.getMaterial()).isEqualTo(hkjMaterial);

        hkjMaterial.removeImages(hkjMaterialImageBack);
        assertThat(hkjMaterial.getImages()).doesNotContain(hkjMaterialImageBack);
        assertThat(hkjMaterialImageBack.getMaterial()).isNull();

        hkjMaterial.images(new HashSet<>(Set.of(hkjMaterialImageBack)));
        assertThat(hkjMaterial.getImages()).containsOnly(hkjMaterialImageBack);
        assertThat(hkjMaterialImageBack.getMaterial()).isEqualTo(hkjMaterial);

        hkjMaterial.setImages(new HashSet<>());
        assertThat(hkjMaterial.getImages()).doesNotContain(hkjMaterialImageBack);
        assertThat(hkjMaterialImageBack.getMaterial()).isNull();
    }
}
