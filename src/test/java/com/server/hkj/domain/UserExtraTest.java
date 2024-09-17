package com.server.hkj.domain;

import static com.server.hkj.domain.HkjEmployeeTestSamples.*;
import static com.server.hkj.domain.UserExtraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
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
    void hkjEmployeeTest() {
        UserExtra userExtra = getUserExtraRandomSampleGenerator();
        HkjEmployee hkjEmployeeBack = getHkjEmployeeRandomSampleGenerator();

        userExtra.setHkjEmployee(hkjEmployeeBack);
        assertThat(userExtra.getHkjEmployee()).isEqualTo(hkjEmployeeBack);
        assertThat(hkjEmployeeBack.getUserExtra()).isEqualTo(userExtra);

        userExtra.hkjEmployee(null);
        assertThat(userExtra.getHkjEmployee()).isNull();
        assertThat(hkjEmployeeBack.getUserExtra()).isNull();
    }
}
