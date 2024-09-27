package com.server.hkj.domain;

import static com.server.hkj.domain.HkjTemplateStepTestSamples.*;
import static com.server.hkj.domain.HkjTemplateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjTemplateStepTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjTemplateStep.class);
        HkjTemplateStep hkjTemplateStep1 = getHkjTemplateStepSample1();
        HkjTemplateStep hkjTemplateStep2 = new HkjTemplateStep();
        assertThat(hkjTemplateStep1).isNotEqualTo(hkjTemplateStep2);

        hkjTemplateStep2.setId(hkjTemplateStep1.getId());
        assertThat(hkjTemplateStep1).isEqualTo(hkjTemplateStep2);

        hkjTemplateStep2 = getHkjTemplateStepSample2();
        assertThat(hkjTemplateStep1).isNotEqualTo(hkjTemplateStep2);
    }

    @Test
    void templateTest() {
        HkjTemplateStep hkjTemplateStep = getHkjTemplateStepRandomSampleGenerator();
        HkjTemplate hkjTemplateBack = getHkjTemplateRandomSampleGenerator();

        hkjTemplateStep.setTemplate(hkjTemplateBack);
        assertThat(hkjTemplateStep.getTemplate()).isEqualTo(hkjTemplateBack);

        hkjTemplateStep.template(null);
        assertThat(hkjTemplateStep.getTemplate()).isNull();
    }
}
