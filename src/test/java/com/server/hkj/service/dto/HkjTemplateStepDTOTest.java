package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjTemplateStepDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjTemplateStepDTO.class);
        HkjTemplateStepDTO hkjTemplateStepDTO1 = new HkjTemplateStepDTO();
        hkjTemplateStepDTO1.setId(1L);
        HkjTemplateStepDTO hkjTemplateStepDTO2 = new HkjTemplateStepDTO();
        assertThat(hkjTemplateStepDTO1).isNotEqualTo(hkjTemplateStepDTO2);
        hkjTemplateStepDTO2.setId(hkjTemplateStepDTO1.getId());
        assertThat(hkjTemplateStepDTO1).isEqualTo(hkjTemplateStepDTO2);
        hkjTemplateStepDTO2.setId(2L);
        assertThat(hkjTemplateStepDTO1).isNotEqualTo(hkjTemplateStepDTO2);
        hkjTemplateStepDTO1.setId(null);
        assertThat(hkjTemplateStepDTO1).isNotEqualTo(hkjTemplateStepDTO2);
    }
}
