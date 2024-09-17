package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjTempImageAsserts.*;
import static com.server.hkj.domain.HkjTempImageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjTempImageMapperTest {

    private HkjTempImageMapper hkjTempImageMapper;

    @BeforeEach
    void setUp() {
        hkjTempImageMapper = new HkjTempImageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjTempImageSample1();
        var actual = hkjTempImageMapper.toEntity(hkjTempImageMapper.toDto(expected));
        assertHkjTempImageAllPropertiesEquals(expected, actual);
    }
}
