package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjPositionAsserts.*;
import static com.server.hkj.domain.HkjPositionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjPositionMapperTest {

    private HkjPositionMapper hkjPositionMapper;

    @BeforeEach
    void setUp() {
        hkjPositionMapper = new HkjPositionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjPositionSample1();
        var actual = hkjPositionMapper.toEntity(hkjPositionMapper.toDto(expected));
        assertHkjPositionAllPropertiesEquals(expected, actual);
    }
}
