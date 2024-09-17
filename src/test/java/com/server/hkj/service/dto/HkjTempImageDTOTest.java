package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjTempImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjTempImageDTO.class);
        HkjTempImageDTO hkjTempImageDTO1 = new HkjTempImageDTO();
        hkjTempImageDTO1.setId(1L);
        HkjTempImageDTO hkjTempImageDTO2 = new HkjTempImageDTO();
        assertThat(hkjTempImageDTO1).isNotEqualTo(hkjTempImageDTO2);
        hkjTempImageDTO2.setId(hkjTempImageDTO1.getId());
        assertThat(hkjTempImageDTO1).isEqualTo(hkjTempImageDTO2);
        hkjTempImageDTO2.setId(2L);
        assertThat(hkjTempImageDTO1).isNotEqualTo(hkjTempImageDTO2);
        hkjTempImageDTO1.setId(null);
        assertThat(hkjTempImageDTO1).isNotEqualTo(hkjTempImageDTO2);
    }
}
