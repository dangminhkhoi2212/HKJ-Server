package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjEmployeeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjEmployeeDTO.class);
        HkjEmployeeDTO hkjEmployeeDTO1 = new HkjEmployeeDTO();
        hkjEmployeeDTO1.setId(1L);
        HkjEmployeeDTO hkjEmployeeDTO2 = new HkjEmployeeDTO();
        assertThat(hkjEmployeeDTO1).isNotEqualTo(hkjEmployeeDTO2);
        hkjEmployeeDTO2.setId(hkjEmployeeDTO1.getId());
        assertThat(hkjEmployeeDTO1).isEqualTo(hkjEmployeeDTO2);
        hkjEmployeeDTO2.setId(2L);
        assertThat(hkjEmployeeDTO1).isNotEqualTo(hkjEmployeeDTO2);
        hkjEmployeeDTO1.setId(null);
        assertThat(hkjEmployeeDTO1).isNotEqualTo(hkjEmployeeDTO2);
    }
}
