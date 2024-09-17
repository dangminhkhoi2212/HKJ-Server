package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjTemplateAsserts.*;
import static com.server.hkj.domain.HkjTemplateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjTemplateMapperTest {

    private HkjTemplateMapper hkjTemplateMapper;

    @BeforeEach
    void setUp() {
        hkjTemplateMapper = new HkjTemplateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjTemplateSample1();
        var actual = hkjTemplateMapper.toEntity(hkjTemplateMapper.toDto(expected));
        assertHkjTemplateAllPropertiesEquals(expected, actual);
    }
}
