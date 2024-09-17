package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjJewelryImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjJewelryImageDTO.class);
        HkjJewelryImageDTO hkjJewelryImageDTO1 = new HkjJewelryImageDTO();
        hkjJewelryImageDTO1.setId(1L);
        HkjJewelryImageDTO hkjJewelryImageDTO2 = new HkjJewelryImageDTO();
        assertThat(hkjJewelryImageDTO1).isNotEqualTo(hkjJewelryImageDTO2);
        hkjJewelryImageDTO2.setId(hkjJewelryImageDTO1.getId());
        assertThat(hkjJewelryImageDTO1).isEqualTo(hkjJewelryImageDTO2);
        hkjJewelryImageDTO2.setId(2L);
        assertThat(hkjJewelryImageDTO1).isNotEqualTo(hkjJewelryImageDTO2);
        hkjJewelryImageDTO1.setId(null);
        assertThat(hkjJewelryImageDTO1).isNotEqualTo(hkjJewelryImageDTO2);
    }
}
