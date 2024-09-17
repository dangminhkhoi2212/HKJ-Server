package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjJewelryModelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjJewelryModelDTO.class);
        HkjJewelryModelDTO hkjJewelryModelDTO1 = new HkjJewelryModelDTO();
        hkjJewelryModelDTO1.setId(1L);
        HkjJewelryModelDTO hkjJewelryModelDTO2 = new HkjJewelryModelDTO();
        assertThat(hkjJewelryModelDTO1).isNotEqualTo(hkjJewelryModelDTO2);
        hkjJewelryModelDTO2.setId(hkjJewelryModelDTO1.getId());
        assertThat(hkjJewelryModelDTO1).isEqualTo(hkjJewelryModelDTO2);
        hkjJewelryModelDTO2.setId(2L);
        assertThat(hkjJewelryModelDTO1).isNotEqualTo(hkjJewelryModelDTO2);
        hkjJewelryModelDTO1.setId(null);
        assertThat(hkjJewelryModelDTO1).isNotEqualTo(hkjJewelryModelDTO2);
    }
}
