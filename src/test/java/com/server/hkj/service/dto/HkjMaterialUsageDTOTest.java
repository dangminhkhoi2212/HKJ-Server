package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjMaterialUsageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjMaterialUsageDTO.class);
        HkjMaterialUsageDTO hkjMaterialUsageDTO1 = new HkjMaterialUsageDTO();
        hkjMaterialUsageDTO1.setId(1L);
        HkjMaterialUsageDTO hkjMaterialUsageDTO2 = new HkjMaterialUsageDTO();
        assertThat(hkjMaterialUsageDTO1).isNotEqualTo(hkjMaterialUsageDTO2);
        hkjMaterialUsageDTO2.setId(hkjMaterialUsageDTO1.getId());
        assertThat(hkjMaterialUsageDTO1).isEqualTo(hkjMaterialUsageDTO2);
        hkjMaterialUsageDTO2.setId(2L);
        assertThat(hkjMaterialUsageDTO1).isNotEqualTo(hkjMaterialUsageDTO2);
        hkjMaterialUsageDTO1.setId(null);
        assertThat(hkjMaterialUsageDTO1).isNotEqualTo(hkjMaterialUsageDTO2);
    }
}
