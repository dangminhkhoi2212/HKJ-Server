package com.server.hkj.domain;

import static com.server.hkj.domain.HkjSalaryTestSamples.*;
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
        assertThat(hkjSalaryBack.getUserExtra()).isEqualTo(userExtra);

        userExtra.removeSalarys(hkjSalaryBack);
        assertThat(userExtra.getSalarys()).doesNotContain(hkjSalaryBack);
        assertThat(hkjSalaryBack.getUserExtra()).isNull();

        userExtra.salarys(new HashSet<>(Set.of(hkjSalaryBack)));
        assertThat(userExtra.getSalarys()).containsOnly(hkjSalaryBack);
        assertThat(hkjSalaryBack.getUserExtra()).isEqualTo(userExtra);

        userExtra.setSalarys(new HashSet<>());
        assertThat(userExtra.getSalarys()).doesNotContain(hkjSalaryBack);
        assertThat(hkjSalaryBack.getUserExtra()).isNull();
    }
}
