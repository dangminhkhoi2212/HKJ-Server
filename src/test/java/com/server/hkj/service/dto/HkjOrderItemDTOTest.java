package com.server.hkj.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjOrderItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjOrderItemDTO.class);
        HkjOrderItemDTO hkjOrderItemDTO1 = new HkjOrderItemDTO();
        hkjOrderItemDTO1.setId(1L);
        HkjOrderItemDTO hkjOrderItemDTO2 = new HkjOrderItemDTO();
        assertThat(hkjOrderItemDTO1).isNotEqualTo(hkjOrderItemDTO2);
        hkjOrderItemDTO2.setId(hkjOrderItemDTO1.getId());
        assertThat(hkjOrderItemDTO1).isEqualTo(hkjOrderItemDTO2);
        hkjOrderItemDTO2.setId(2L);
        assertThat(hkjOrderItemDTO1).isNotEqualTo(hkjOrderItemDTO2);
        hkjOrderItemDTO1.setId(null);
        assertThat(hkjOrderItemDTO1).isNotEqualTo(hkjOrderItemDTO2);
    }
}
