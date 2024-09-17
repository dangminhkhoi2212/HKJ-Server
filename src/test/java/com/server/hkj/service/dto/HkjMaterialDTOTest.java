package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjMaterialDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjMaterialDTO.class);
        HkjMaterialDTO hkjMaterialDTO1 = new HkjMaterialDTO();
        hkjMaterialDTO1.setId(1L);
        HkjMaterialDTO hkjMaterialDTO2 = new HkjMaterialDTO();
        assertThat(hkjMaterialDTO1).isNotEqualTo(hkjMaterialDTO2);
        hkjMaterialDTO2.setId(hkjMaterialDTO1.getId());
        assertThat(hkjMaterialDTO1).isEqualTo(hkjMaterialDTO2);
        hkjMaterialDTO2.setId(2L);
        assertThat(hkjMaterialDTO1).isNotEqualTo(hkjMaterialDTO2);
        hkjMaterialDTO1.setId(null);
        assertThat(hkjMaterialDTO1).isNotEqualTo(hkjMaterialDTO2);
    }
}
