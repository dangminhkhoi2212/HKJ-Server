package com.server.hkj.domain;

import static com.server.hkj.domain.HkjCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjCategory.class);
        HkjCategory hkjCategory1 = getHkjCategorySample1();
        HkjCategory hkjCategory2 = new HkjCategory();
        assertThat(hkjCategory1).isNotEqualTo(hkjCategory2);

        hkjCategory2.setId(hkjCategory1.getId());
        assertThat(hkjCategory1).isEqualTo(hkjCategory2);

        hkjCategory2 = getHkjCategorySample2();
        assertThat(hkjCategory1).isNotEqualTo(hkjCategory2);
    }
}
