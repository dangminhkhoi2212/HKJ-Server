package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjHireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjHireDTO.class);
        HkjHireDTO hkjHireDTO1 = new HkjHireDTO();
        hkjHireDTO1.setId(1L);
        HkjHireDTO hkjHireDTO2 = new HkjHireDTO();
        assertThat(hkjHireDTO1).isNotEqualTo(hkjHireDTO2);
        hkjHireDTO2.setId(hkjHireDTO1.getId());
        assertThat(hkjHireDTO1).isEqualTo(hkjHireDTO2);
        hkjHireDTO2.setId(2L);
        assertThat(hkjHireDTO1).isNotEqualTo(hkjHireDTO2);
        hkjHireDTO1.setId(null);
        assertThat(hkjHireDTO1).isNotEqualTo(hkjHireDTO2);
    }
}
