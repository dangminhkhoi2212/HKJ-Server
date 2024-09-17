package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjTaskImageAsserts.*;
import static com.server.hkj.domain.HkjTaskImageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjTaskImageMapperTest {

    private HkjTaskImageMapper hkjTaskImageMapper;

    @BeforeEach
    void setUp() {
        hkjTaskImageMapper = new HkjTaskImageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjTaskImageSample1();
        var actual = hkjTaskImageMapper.toEntity(hkjTaskImageMapper.toDto(expected));
        assertHkjTaskImageAllPropertiesEquals(expected, actual);
    }
}
