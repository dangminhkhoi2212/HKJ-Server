package com.server.hkj.domain;

import static com.server.hkj.domain.HkjTempImageTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjTempImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjTempImage.class);
        HkjTempImage hkjTempImage1 = getHkjTempImageSample1();
        HkjTempImage hkjTempImage2 = new HkjTempImage();
        assertThat(hkjTempImage1).isNotEqualTo(hkjTempImage2);

        hkjTempImage2.setId(hkjTempImage1.getId());
        assertThat(hkjTempImage1).isEqualTo(hkjTempImage2);

        hkjTempImage2 = getHkjTempImageSample2();
        assertThat(hkjTempImage1).isNotEqualTo(hkjTempImage2);
    }
}
