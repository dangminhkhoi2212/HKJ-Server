package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjMaterialImageAsserts.*;
import static com.server.hkj.domain.HkjMaterialImageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjMaterialImageMapperTest {

    private HkjMaterialImageMapper hkjMaterialImageMapper;

    @BeforeEach
    void setUp() {
        hkjMaterialImageMapper = new HkjMaterialImageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjMaterialImageSample1();
        var actual = hkjMaterialImageMapper.toEntity(hkjMaterialImageMapper.toDto(expected));
        assertHkjMaterialImageAllPropertiesEquals(expected, actual);
    }
}
