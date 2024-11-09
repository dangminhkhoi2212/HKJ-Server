package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjCartDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjCartDTO.class);
        HkjCartDTO hkjCartDTO1 = new HkjCartDTO();
        hkjCartDTO1.setId(1L);
        HkjCartDTO hkjCartDTO2 = new HkjCartDTO();
        assertThat(hkjCartDTO1).isNotEqualTo(hkjCartDTO2);
        hkjCartDTO2.setId(hkjCartDTO1.getId());
        assertThat(hkjCartDTO1).isEqualTo(hkjCartDTO2);
        hkjCartDTO2.setId(2L);
        assertThat(hkjCartDTO1).isNotEqualTo(hkjCartDTO2);
        hkjCartDTO1.setId(null);
        assertThat(hkjCartDTO1).isNotEqualTo(hkjCartDTO2);
    }
}
