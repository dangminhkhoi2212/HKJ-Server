package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjOrderDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjOrderDTO.class);
        HkjOrderDTO hkjOrderDTO1 = new HkjOrderDTO();
        hkjOrderDTO1.setId(1L);
        HkjOrderDTO hkjOrderDTO2 = new HkjOrderDTO();
        assertThat(hkjOrderDTO1).isNotEqualTo(hkjOrderDTO2);
        hkjOrderDTO2.setId(hkjOrderDTO1.getId());
        assertThat(hkjOrderDTO1).isEqualTo(hkjOrderDTO2);
        hkjOrderDTO2.setId(2L);
        assertThat(hkjOrderDTO1).isNotEqualTo(hkjOrderDTO2);
        hkjOrderDTO1.setId(null);
        assertThat(hkjOrderDTO1).isNotEqualTo(hkjOrderDTO2);
    }
}
