package com.server.hkj.domain;

import static com.server.hkj.domain.HkjHireTestSamples.*;
import static com.server.hkj.domain.HkjPositionTestSamples.*;
import static com.server.hkj.domain.UserExtraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjHireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjHire.class);
        HkjHire hkjHire1 = getHkjHireSample1();
        HkjHire hkjHire2 = new HkjHire();
        assertThat(hkjHire1).isNotEqualTo(hkjHire2);

        hkjHire2.setId(hkjHire1.getId());
        assertThat(hkjHire1).isEqualTo(hkjHire2);

        hkjHire2 = getHkjHireSample2();
        assertThat(hkjHire1).isNotEqualTo(hkjHire2);
    }

    @Test
    void positionTest() {
        HkjHire hkjHire = getHkjHireRandomSampleGenerator();
        HkjPosition hkjPositionBack = getHkjPositionRandomSampleGenerator();

        hkjHire.setPosition(hkjPositionBack);
        assertThat(hkjHire.getPosition()).isEqualTo(hkjPositionBack);

        hkjHire.position(null);
        assertThat(hkjHire.getPosition()).isNull();
    }

    @Test
    void employeeTest() {
        HkjHire hkjHire = getHkjHireRandomSampleGenerator();
        UserExtra userExtraBack = getUserExtraRandomSampleGenerator();

        hkjHire.setEmployee(userExtraBack);
        assertThat(hkjHire.getEmployee()).isEqualTo(userExtraBack);

        hkjHire.employee(null);
        assertThat(hkjHire.getEmployee()).isNull();
    }
}
