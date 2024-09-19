package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjOrderImageAsserts.*;
import static com.server.hkj.domain.HkjOrderImageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjOrderImageMapperTest {

    private HkjOrderImageMapper hkjOrderImageMapper;

    @BeforeEach
    void setUp() {
        hkjOrderImageMapper = new HkjOrderImageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjOrderImageSample1();
        var actual = hkjOrderImageMapper.toEntity(hkjOrderImageMapper.toDto(expected));
        assertHkjOrderImageAllPropertiesEquals(expected, actual);
    }
}
