package com.server.hkj.domain;

import static com.server.hkj.domain.HkjJewelryModelTestSamples.*;
import static com.server.hkj.domain.HkjTrackSearchImageTestSamples.*;
import static com.server.hkj.domain.UserExtraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjTrackSearchImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjTrackSearchImage.class);
        HkjTrackSearchImage hkjTrackSearchImage1 = getHkjTrackSearchImageSample1();
        HkjTrackSearchImage hkjTrackSearchImage2 = new HkjTrackSearchImage();
        assertThat(hkjTrackSearchImage1).isNotEqualTo(hkjTrackSearchImage2);

        hkjTrackSearchImage2.setId(hkjTrackSearchImage1.getId());
        assertThat(hkjTrackSearchImage1).isEqualTo(hkjTrackSearchImage2);

        hkjTrackSearchImage2 = getHkjTrackSearchImageSample2();
        assertThat(hkjTrackSearchImage1).isNotEqualTo(hkjTrackSearchImage2);
    }

    @Test
    void userTest() {
        HkjTrackSearchImage hkjTrackSearchImage = getHkjTrackSearchImageRandomSampleGenerator();
        UserExtra userExtraBack = getUserExtraRandomSampleGenerator();

        hkjTrackSearchImage.setUser(userExtraBack);
        assertThat(hkjTrackSearchImage.getUser()).isEqualTo(userExtraBack);

        hkjTrackSearchImage.user(null);
        assertThat(hkjTrackSearchImage.getUser()).isNull();
    }

    @Test
    void jewelryTest() {
        HkjTrackSearchImage hkjTrackSearchImage = getHkjTrackSearchImageRandomSampleGenerator();
        HkjJewelryModel hkjJewelryModelBack = getHkjJewelryModelRandomSampleGenerator();

        hkjTrackSearchImage.setJewelry(hkjJewelryModelBack);
        assertThat(hkjTrackSearchImage.getJewelry()).isEqualTo(hkjJewelryModelBack);

        hkjTrackSearchImage.jewelry(null);
        assertThat(hkjTrackSearchImage.getJewelry()).isNull();
    }
}
