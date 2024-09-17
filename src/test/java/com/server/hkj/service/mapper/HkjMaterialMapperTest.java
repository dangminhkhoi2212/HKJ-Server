package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjMaterialAsserts.*;
import static com.server.hkj.domain.HkjMaterialTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjMaterialMapperTest {

    private HkjMaterialMapper hkjMaterialMapper;

    @BeforeEach
    void setUp() {
        hkjMaterialMapper = new HkjMaterialMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjMaterialSample1();
        var actual = hkjMaterialMapper.toEntity(hkjMaterialMapper.toDto(expected));
        assertHkjMaterialAllPropertiesEquals(expected, actual);
    }
}
