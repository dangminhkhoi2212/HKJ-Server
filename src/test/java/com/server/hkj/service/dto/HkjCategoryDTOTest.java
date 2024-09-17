package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjCategoryDTO.class);
        HkjCategoryDTO hkjCategoryDTO1 = new HkjCategoryDTO();
        hkjCategoryDTO1.setId(1L);
        HkjCategoryDTO hkjCategoryDTO2 = new HkjCategoryDTO();
        assertThat(hkjCategoryDTO1).isNotEqualTo(hkjCategoryDTO2);
        hkjCategoryDTO2.setId(hkjCategoryDTO1.getId());
        assertThat(hkjCategoryDTO1).isEqualTo(hkjCategoryDTO2);
        hkjCategoryDTO2.setId(2L);
        assertThat(hkjCategoryDTO1).isNotEqualTo(hkjCategoryDTO2);
        hkjCategoryDTO1.setId(null);
        assertThat(hkjCategoryDTO1).isNotEqualTo(hkjCategoryDTO2);
    }
}
