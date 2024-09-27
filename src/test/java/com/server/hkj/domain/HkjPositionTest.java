package com.server.hkj.domain;

import static com.server.hkj.domain.HkjHireTestSamples.*;
import static com.server.hkj.domain.HkjPositionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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

    @Test
    void hireTest() {
        HkjPosition hkjPosition = getHkjPositionRandomSampleGenerator();
        HkjHire hkjHireBack = getHkjHireRandomSampleGenerator();

        hkjPosition.addHire(hkjHireBack);
        assertThat(hkjPosition.getHires()).containsOnly(hkjHireBack);
        assertThat(hkjHireBack.getPosition()).isEqualTo(hkjPosition);

        hkjPosition.removeHire(hkjHireBack);
        assertThat(hkjPosition.getHires()).doesNotContain(hkjHireBack);
        assertThat(hkjHireBack.getPosition()).isNull();

        hkjPosition.hires(new HashSet<>(Set.of(hkjHireBack)));
        assertThat(hkjPosition.getHires()).containsOnly(hkjHireBack);
        assertThat(hkjHireBack.getPosition()).isEqualTo(hkjPosition);

        hkjPosition.setHires(new HashSet<>());
        assertThat(hkjPosition.getHires()).doesNotContain(hkjHireBack);
        assertThat(hkjHireBack.getPosition()).isNull();
    }
}
