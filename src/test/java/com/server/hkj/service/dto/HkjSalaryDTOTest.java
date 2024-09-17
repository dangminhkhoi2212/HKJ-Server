package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjSalaryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjSalaryDTO.class);
        HkjSalaryDTO hkjSalaryDTO1 = new HkjSalaryDTO();
        hkjSalaryDTO1.setId(1L);
        HkjSalaryDTO hkjSalaryDTO2 = new HkjSalaryDTO();
        assertThat(hkjSalaryDTO1).isNotEqualTo(hkjSalaryDTO2);
        hkjSalaryDTO2.setId(hkjSalaryDTO1.getId());
        assertThat(hkjSalaryDTO1).isEqualTo(hkjSalaryDTO2);
        hkjSalaryDTO2.setId(2L);
        assertThat(hkjSalaryDTO1).isNotEqualTo(hkjSalaryDTO2);
        hkjSalaryDTO1.setId(null);
        assertThat(hkjSalaryDTO1).isNotEqualTo(hkjSalaryDTO2);
    }
}
