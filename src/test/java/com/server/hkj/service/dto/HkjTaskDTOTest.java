package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjTaskDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjTaskDTO.class);
        HkjTaskDTO hkjTaskDTO1 = new HkjTaskDTO();
        hkjTaskDTO1.setId(1L);
        HkjTaskDTO hkjTaskDTO2 = new HkjTaskDTO();
        assertThat(hkjTaskDTO1).isNotEqualTo(hkjTaskDTO2);
        hkjTaskDTO2.setId(hkjTaskDTO1.getId());
        assertThat(hkjTaskDTO1).isEqualTo(hkjTaskDTO2);
        hkjTaskDTO2.setId(2L);
        assertThat(hkjTaskDTO1).isNotEqualTo(hkjTaskDTO2);
        hkjTaskDTO1.setId(null);
        assertThat(hkjTaskDTO1).isNotEqualTo(hkjTaskDTO2);
    }
}
