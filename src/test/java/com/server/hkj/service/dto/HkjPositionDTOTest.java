package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjPositionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjPositionDTO.class);
        HkjPositionDTO hkjPositionDTO1 = new HkjPositionDTO();
        hkjPositionDTO1.setId(1L);
        HkjPositionDTO hkjPositionDTO2 = new HkjPositionDTO();
        assertThat(hkjPositionDTO1).isNotEqualTo(hkjPositionDTO2);
        hkjPositionDTO2.setId(hkjPositionDTO1.getId());
        assertThat(hkjPositionDTO1).isEqualTo(hkjPositionDTO2);
        hkjPositionDTO2.setId(2L);
        assertThat(hkjPositionDTO1).isNotEqualTo(hkjPositionDTO2);
        hkjPositionDTO1.setId(null);
        assertThat(hkjPositionDTO1).isNotEqualTo(hkjPositionDTO2);
    }
}
