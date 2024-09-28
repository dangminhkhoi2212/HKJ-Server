package com.server.hkj.domain;

import static com.server.hkj.domain.HkjSalaryTestSamples.*;
import static com.server.hkj.domain.UserExtraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjSalaryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjSalary.class);
        HkjSalary hkjSalary1 = getHkjSalarySample1();
        HkjSalary hkjSalary2 = new HkjSalary();
        assertThat(hkjSalary1).isNotEqualTo(hkjSalary2);

        hkjSalary2.setId(hkjSalary1.getId());
        assertThat(hkjSalary1).isEqualTo(hkjSalary2);

        hkjSalary2 = getHkjSalarySample2();
        assertThat(hkjSalary1).isNotEqualTo(hkjSalary2);
    }

    @Test
    void userExtraTest() {
        HkjSalary hkjSalary = getHkjSalaryRandomSampleGenerator();
        UserExtra userExtraBack = getUserExtraRandomSampleGenerator();

        hkjSalary.setUserExtra(userExtraBack);
        assertThat(hkjSalary.getUserExtra()).isEqualTo(userExtraBack);

        hkjSalary.userExtra(null);
        assertThat(hkjSalary.getUserExtra()).isNull();
    }
}
