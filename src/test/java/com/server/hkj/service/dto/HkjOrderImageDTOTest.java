package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjOrderImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjOrderImageDTO.class);
        HkjOrderImageDTO hkjOrderImageDTO1 = new HkjOrderImageDTO();
        hkjOrderImageDTO1.setId(1L);
        HkjOrderImageDTO hkjOrderImageDTO2 = new HkjOrderImageDTO();
        assertThat(hkjOrderImageDTO1).isNotEqualTo(hkjOrderImageDTO2);
        hkjOrderImageDTO2.setId(hkjOrderImageDTO1.getId());
        assertThat(hkjOrderImageDTO1).isEqualTo(hkjOrderImageDTO2);
        hkjOrderImageDTO2.setId(2L);
        assertThat(hkjOrderImageDTO1).isNotEqualTo(hkjOrderImageDTO2);
        hkjOrderImageDTO1.setId(null);
        assertThat(hkjOrderImageDTO1).isNotEqualTo(hkjOrderImageDTO2);
    }
}
