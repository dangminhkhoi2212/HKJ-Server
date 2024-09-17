package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjTemplateStepAsserts.*;
import static com.server.hkj.domain.HkjTemplateStepTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjTemplateStepMapperTest {

    private HkjTemplateStepMapper hkjTemplateStepMapper;

    @BeforeEach
    void setUp() {
        hkjTemplateStepMapper = new HkjTemplateStepMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjTemplateStepSample1();
        var actual = hkjTemplateStepMapper.toEntity(hkjTemplateStepMapper.toDto(expected));
        assertHkjTemplateStepAllPropertiesEquals(expected, actual);
    }
}
