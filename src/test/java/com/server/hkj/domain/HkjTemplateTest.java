package com.server.hkj.domain;

import static com.server.hkj.domain.HkjCategoryTestSamples.*;
import static com.server.hkj.domain.HkjTemplateStepTestSamples.*;
import static com.server.hkj.domain.HkjTemplateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HkjTemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjTemplate.class);
        HkjTemplate hkjTemplate1 = getHkjTemplateSample1();
        HkjTemplate hkjTemplate2 = new HkjTemplate();
        assertThat(hkjTemplate1).isNotEqualTo(hkjTemplate2);

        hkjTemplate2.setId(hkjTemplate1.getId());
        assertThat(hkjTemplate1).isEqualTo(hkjTemplate2);

        hkjTemplate2 = getHkjTemplateSample2();
        assertThat(hkjTemplate1).isNotEqualTo(hkjTemplate2);
    }

    @Test
    void stepsTest() {
        HkjTemplate hkjTemplate = getHkjTemplateRandomSampleGenerator();
        HkjTemplateStep hkjTemplateStepBack = getHkjTemplateStepRandomSampleGenerator();

        hkjTemplate.addSteps(hkjTemplateStepBack);
        assertThat(hkjTemplate.getSteps()).containsOnly(hkjTemplateStepBack);
        assertThat(hkjTemplateStepBack.getHkjTemplate()).isEqualTo(hkjTemplate);

        hkjTemplate.removeSteps(hkjTemplateStepBack);
        assertThat(hkjTemplate.getSteps()).doesNotContain(hkjTemplateStepBack);
        assertThat(hkjTemplateStepBack.getHkjTemplate()).isNull();

        hkjTemplate.steps(new HashSet<>(Set.of(hkjTemplateStepBack)));
        assertThat(hkjTemplate.getSteps()).containsOnly(hkjTemplateStepBack);
        assertThat(hkjTemplateStepBack.getHkjTemplate()).isEqualTo(hkjTemplate);

        hkjTemplate.setSteps(new HashSet<>());
        assertThat(hkjTemplate.getSteps()).doesNotContain(hkjTemplateStepBack);
        assertThat(hkjTemplateStepBack.getHkjTemplate()).isNull();
    }

    @Test
    void categoryTest() {
        HkjTemplate hkjTemplate = getHkjTemplateRandomSampleGenerator();
        HkjCategory hkjCategoryBack = getHkjCategoryRandomSampleGenerator();

        hkjTemplate.setCategory(hkjCategoryBack);
        assertThat(hkjTemplate.getCategory()).isEqualTo(hkjCategoryBack);

        hkjTemplate.category(null);
        assertThat(hkjTemplate.getCategory()).isNull();
    }
}
