package com.server.hkj.domain;

import static com.server.hkj.domain.HkjHireTestSamples.*;
import static com.server.hkj.domain.HkjSalaryTestSamples.*;
import static com.server.hkj.domain.HkjTaskTestSamples.*;
import static com.server.hkj.domain.UserExtraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserExtraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserExtra.class);
        UserExtra userExtra1 = getUserExtraSample1();
        UserExtra userExtra2 = new UserExtra();
        assertThat(userExtra1).isNotEqualTo(userExtra2);

        userExtra2.setId(userExtra1.getId());
        assertThat(userExtra1).isEqualTo(userExtra2);

        userExtra2 = getUserExtraSample2();
        assertThat(userExtra1).isNotEqualTo(userExtra2);
    }

    @Test
    void salarysTest() {
        UserExtra userExtra = getUserExtraRandomSampleGenerator();
        HkjSalary hkjSalaryBack = getHkjSalaryRandomSampleGenerator();

        userExtra.addSalarys(hkjSalaryBack);
        assertThat(userExtra.getSalarys()).containsOnly(hkjSalaryBack);
        assertThat(hkjSalaryBack.getEmployee()).isEqualTo(userExtra);

        userExtra.removeSalarys(hkjSalaryBack);
        assertThat(userExtra.getSalarys()).doesNotContain(hkjSalaryBack);
        assertThat(hkjSalaryBack.getEmployee()).isNull();

        userExtra.salarys(new HashSet<>(Set.of(hkjSalaryBack)));
        assertThat(userExtra.getSalarys()).containsOnly(hkjSalaryBack);
        assertThat(hkjSalaryBack.getEmployee()).isEqualTo(userExtra);

        userExtra.setSalarys(new HashSet<>());
        assertThat(userExtra.getSalarys()).doesNotContain(hkjSalaryBack);
        assertThat(hkjSalaryBack.getEmployee()).isNull();
    }

    @Test
    void hireTest() {
        UserExtra userExtra = getUserExtraRandomSampleGenerator();
        HkjHire hkjHireBack = getHkjHireRandomSampleGenerator();

        userExtra.addHire(hkjHireBack);
        assertThat(userExtra.getHires()).containsOnly(hkjHireBack);
        assertThat(hkjHireBack.getEmployee()).isEqualTo(userExtra);

        userExtra.removeHire(hkjHireBack);
        assertThat(userExtra.getHires()).doesNotContain(hkjHireBack);
        assertThat(hkjHireBack.getEmployee()).isNull();

        userExtra.hires(new HashSet<>(Set.of(hkjHireBack)));
        assertThat(userExtra.getHires()).containsOnly(hkjHireBack);
        assertThat(hkjHireBack.getEmployee()).isEqualTo(userExtra);

        userExtra.setHires(new HashSet<>());
        assertThat(userExtra.getHires()).doesNotContain(hkjHireBack);
        assertThat(hkjHireBack.getEmployee()).isNull();
    }

    @Test
    void hkjTaskTest() {
        UserExtra userExtra = getUserExtraRandomSampleGenerator();
        HkjTask hkjTaskBack = getHkjTaskRandomSampleGenerator();

        userExtra.setHkjTask(hkjTaskBack);
        assertThat(userExtra.getHkjTask()).isEqualTo(hkjTaskBack);
        assertThat(hkjTaskBack.getEmployee()).isEqualTo(userExtra);

        userExtra.hkjTask(null);
        assertThat(userExtra.getHkjTask()).isNull();
        assertThat(hkjTaskBack.getEmployee()).isNull();
    }
}
