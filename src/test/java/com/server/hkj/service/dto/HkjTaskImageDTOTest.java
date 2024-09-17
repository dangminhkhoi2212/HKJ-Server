package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjTaskImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjTaskImageDTO.class);
        HkjTaskImageDTO hkjTaskImageDTO1 = new HkjTaskImageDTO();
        hkjTaskImageDTO1.setId(1L);
        HkjTaskImageDTO hkjTaskImageDTO2 = new HkjTaskImageDTO();
        assertThat(hkjTaskImageDTO1).isNotEqualTo(hkjTaskImageDTO2);
        hkjTaskImageDTO2.setId(hkjTaskImageDTO1.getId());
        assertThat(hkjTaskImageDTO1).isEqualTo(hkjTaskImageDTO2);
        hkjTaskImageDTO2.setId(2L);
        assertThat(hkjTaskImageDTO1).isNotEqualTo(hkjTaskImageDTO2);
        hkjTaskImageDTO1.setId(null);
        assertThat(hkjTaskImageDTO1).isNotEqualTo(hkjTaskImageDTO2);
    }
}
