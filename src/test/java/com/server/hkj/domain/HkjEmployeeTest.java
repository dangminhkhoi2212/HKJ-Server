package com.server.hkj.domain;

import static com.server.hkj.domain.HkjEmployeeTestSamples.*;
import static com.server.hkj.domain.HkjHireTestSamples.*;
import static com.server.hkj.domain.HkjSalaryTestSamples.*;
import static com.server.hkj.domain.UserExtraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HkjEmployeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjEmployee.class);
        HkjEmployee hkjEmployee1 = getHkjEmployeeSample1();
        HkjEmployee hkjEmployee2 = new HkjEmployee();
        assertThat(hkjEmployee1).isNotEqualTo(hkjEmployee2);

        hkjEmployee2.setId(hkjEmployee1.getId());
        assertThat(hkjEmployee1).isEqualTo(hkjEmployee2);

        hkjEmployee2 = getHkjEmployeeSample2();
        assertThat(hkjEmployee1).isNotEqualTo(hkjEmployee2);
    }

    @Test
    void userExtraTest() {
        HkjEmployee hkjEmployee = getHkjEmployeeRandomSampleGenerator();
        UserExtra userExtraBack = getUserExtraRandomSampleGenerator();

        hkjEmployee.setUserExtra(userExtraBack);
        assertThat(hkjEmployee.getUserExtra()).isEqualTo(userExtraBack);

        hkjEmployee.userExtra(null);
        assertThat(hkjEmployee.getUserExtra()).isNull();
    }

    @Test
    void salarysTest() {
        HkjEmployee hkjEmployee = getHkjEmployeeRandomSampleGenerator();
        HkjSalary hkjSalaryBack = getHkjSalaryRandomSampleGenerator();

        hkjEmployee.addSalarys(hkjSalaryBack);
        assertThat(hkjEmployee.getSalarys()).containsOnly(hkjSalaryBack);
        assertThat(hkjSalaryBack.getHkjEmployee()).isEqualTo(hkjEmployee);

        hkjEmployee.removeSalarys(hkjSalaryBack);
        assertThat(hkjEmployee.getSalarys()).doesNotContain(hkjSalaryBack);
        assertThat(hkjSalaryBack.getHkjEmployee()).isNull();

        hkjEmployee.salarys(new HashSet<>(Set.of(hkjSalaryBack)));
        assertThat(hkjEmployee.getSalarys()).containsOnly(hkjSalaryBack);
        assertThat(hkjSalaryBack.getHkjEmployee()).isEqualTo(hkjEmployee);

        hkjEmployee.setSalarys(new HashSet<>());
        assertThat(hkjEmployee.getSalarys()).doesNotContain(hkjSalaryBack);
        assertThat(hkjSalaryBack.getHkjEmployee()).isNull();
    }

    @Test
    void hkjHireTest() {
        HkjEmployee hkjEmployee = getHkjEmployeeRandomSampleGenerator();
        HkjHire hkjHireBack = getHkjHireRandomSampleGenerator();

        hkjEmployee.setHkjHire(hkjHireBack);
        assertThat(hkjEmployee.getHkjHire()).isEqualTo(hkjHireBack);
        assertThat(hkjHireBack.getEmployee()).isEqualTo(hkjEmployee);

        hkjEmployee.hkjHire(null);
        assertThat(hkjEmployee.getHkjHire()).isNull();
        assertThat(hkjHireBack.getEmployee()).isNull();
    }
}
