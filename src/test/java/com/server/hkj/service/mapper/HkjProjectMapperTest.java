package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjProjectAsserts.*;
import static com.server.hkj.domain.HkjProjectTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjProjectMapperTest {

    private HkjProjectMapper hkjProjectMapper;

    @BeforeEach
    void setUp() {
        hkjProjectMapper = new HkjProjectMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjProjectSample1();
        var actual = hkjProjectMapper.toEntity(hkjProjectMapper.toDto(expected));
        assertHkjProjectAllPropertiesEquals(expected, actual);
    }
}
