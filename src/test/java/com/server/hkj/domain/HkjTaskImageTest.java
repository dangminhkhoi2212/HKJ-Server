package com.server.hkj.domain;

import static com.server.hkj.domain.HkjTaskImageTestSamples.*;
import static com.server.hkj.domain.HkjTaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjTaskImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjTaskImage.class);
        HkjTaskImage hkjTaskImage1 = getHkjTaskImageSample1();
        HkjTaskImage hkjTaskImage2 = new HkjTaskImage();
        assertThat(hkjTaskImage1).isNotEqualTo(hkjTaskImage2);

        hkjTaskImage2.setId(hkjTaskImage1.getId());
        assertThat(hkjTaskImage1).isEqualTo(hkjTaskImage2);

        hkjTaskImage2 = getHkjTaskImageSample2();
        assertThat(hkjTaskImage1).isNotEqualTo(hkjTaskImage2);
    }

    @Test
    void taskTest() {
        HkjTaskImage hkjTaskImage = getHkjTaskImageRandomSampleGenerator();
        HkjTask hkjTaskBack = getHkjTaskRandomSampleGenerator();

        hkjTaskImage.setTask(hkjTaskBack);
        assertThat(hkjTaskImage.getTask()).isEqualTo(hkjTaskBack);

        hkjTaskImage.task(null);
        assertThat(hkjTaskImage.getTask()).isNull();
    }
}
