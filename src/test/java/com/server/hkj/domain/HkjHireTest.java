package com.server.hkj.domain;

import static com.server.hkj.domain.HkjEmployeeTestSamples.*;
import static com.server.hkj.domain.HkjHireTestSamples.*;
import static com.server.hkj.domain.HkjPositionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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
    void employeesTest() {
        HkjHire hkjHire = getHkjHireRandomSampleGenerator();
        HkjEmployee hkjEmployeeBack = getHkjEmployeeRandomSampleGenerator();

        hkjHire.addEmployees(hkjEmployeeBack);
        assertThat(hkjHire.getEmployees()).containsOnly(hkjEmployeeBack);
        assertThat(hkjEmployeeBack.getHkjHire()).isEqualTo(hkjHire);

        hkjHire.removeEmployees(hkjEmployeeBack);
        assertThat(hkjHire.getEmployees()).doesNotContain(hkjEmployeeBack);
        assertThat(hkjEmployeeBack.getHkjHire()).isNull();

        hkjHire.employees(new HashSet<>(Set.of(hkjEmployeeBack)));
        assertThat(hkjHire.getEmployees()).containsOnly(hkjEmployeeBack);
        assertThat(hkjEmployeeBack.getHkjHire()).isEqualTo(hkjHire);

        hkjHire.setEmployees(new HashSet<>());
        assertThat(hkjHire.getEmployees()).doesNotContain(hkjEmployeeBack);
        assertThat(hkjEmployeeBack.getHkjHire()).isNull();
    }
}
