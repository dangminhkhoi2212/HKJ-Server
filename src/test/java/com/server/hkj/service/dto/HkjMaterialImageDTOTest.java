package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjMaterialImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjMaterialImageDTO.class);
        HkjMaterialImageDTO hkjMaterialImageDTO1 = new HkjMaterialImageDTO();
        hkjMaterialImageDTO1.setId(1L);
        HkjMaterialImageDTO hkjMaterialImageDTO2 = new HkjMaterialImageDTO();
        assertThat(hkjMaterialImageDTO1).isNotEqualTo(hkjMaterialImageDTO2);
        hkjMaterialImageDTO2.setId(hkjMaterialImageDTO1.getId());
        assertThat(hkjMaterialImageDTO1).isEqualTo(hkjMaterialImageDTO2);
        hkjMaterialImageDTO2.setId(2L);
        assertThat(hkjMaterialImageDTO1).isNotEqualTo(hkjMaterialImageDTO2);
        hkjMaterialImageDTO1.setId(null);
        assertThat(hkjMaterialImageDTO1).isNotEqualTo(hkjMaterialImageDTO2);
    }
}
