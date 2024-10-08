package com.server.hkj.domain;

import static com.server.hkj.domain.HkjPositionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjPositionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjPosition.class);
        HkjPosition hkjPosition1 = getHkjPositionSample1();
        HkjPosition hkjPosition2 = new HkjPosition();
        assertThat(hkjPosition1).isNotEqualTo(hkjPosition2);

        hkjPosition2.setId(hkjPosition1.getId());
        assertThat(hkjPosition1).isEqualTo(hkjPosition2);

        hkjPosition2 = getHkjPositionSample2();
        assertThat(hkjPosition1).isNotEqualTo(hkjPosition2);
    }
}
