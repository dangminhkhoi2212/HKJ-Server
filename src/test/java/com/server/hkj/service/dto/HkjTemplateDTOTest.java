package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjTemplateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjTemplateDTO.class);
        HkjTemplateDTO hkjTemplateDTO1 = new HkjTemplateDTO();
        hkjTemplateDTO1.setId(1L);
        HkjTemplateDTO hkjTemplateDTO2 = new HkjTemplateDTO();
        assertThat(hkjTemplateDTO1).isNotEqualTo(hkjTemplateDTO2);
        hkjTemplateDTO2.setId(hkjTemplateDTO1.getId());
        assertThat(hkjTemplateDTO1).isEqualTo(hkjTemplateDTO2);
        hkjTemplateDTO2.setId(2L);
        assertThat(hkjTemplateDTO1).isNotEqualTo(hkjTemplateDTO2);
        hkjTemplateDTO1.setId(null);
        assertThat(hkjTemplateDTO1).isNotEqualTo(hkjTemplateDTO2);
    }
}
