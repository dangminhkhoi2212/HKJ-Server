package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjTrackSearchImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjTrackSearchImageDTO.class);
        HkjTrackSearchImageDTO hkjTrackSearchImageDTO1 = new HkjTrackSearchImageDTO();
        hkjTrackSearchImageDTO1.setId(1L);
        HkjTrackSearchImageDTO hkjTrackSearchImageDTO2 = new HkjTrackSearchImageDTO();
        assertThat(hkjTrackSearchImageDTO1).isNotEqualTo(hkjTrackSearchImageDTO2);
        hkjTrackSearchImageDTO2.setId(hkjTrackSearchImageDTO1.getId());
        assertThat(hkjTrackSearchImageDTO1).isEqualTo(hkjTrackSearchImageDTO2);
        hkjTrackSearchImageDTO2.setId(2L);
        assertThat(hkjTrackSearchImageDTO1).isNotEqualTo(hkjTrackSearchImageDTO2);
        hkjTrackSearchImageDTO1.setId(null);
        assertThat(hkjTrackSearchImageDTO1).isNotEqualTo(hkjTrackSearchImageDTO2);
    }
}
