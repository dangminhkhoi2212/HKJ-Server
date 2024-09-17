package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjProjectDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjProjectDTO.class);
        HkjProjectDTO hkjProjectDTO1 = new HkjProjectDTO();
        hkjProjectDTO1.setId(1L);
        HkjProjectDTO hkjProjectDTO2 = new HkjProjectDTO();
        assertThat(hkjProjectDTO1).isNotEqualTo(hkjProjectDTO2);
        hkjProjectDTO2.setId(hkjProjectDTO1.getId());
        assertThat(hkjProjectDTO1).isEqualTo(hkjProjectDTO2);
        hkjProjectDTO2.setId(2L);
        assertThat(hkjProjectDTO1).isNotEqualTo(hkjProjectDTO2);
        hkjProjectDTO1.setId(null);
        assertThat(hkjProjectDTO1).isNotEqualTo(hkjProjectDTO2);
    }
}
