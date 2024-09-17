package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjTaskAsserts.*;
import static com.server.hkj.domain.HkjTaskTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjTaskMapperTest {

    private HkjTaskMapper hkjTaskMapper;

    @BeforeEach
    void setUp() {
        hkjTaskMapper = new HkjTaskMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjTaskSample1();
        var actual = hkjTaskMapper.toEntity(hkjTaskMapper.toDto(expected));
        assertHkjTaskAllPropertiesEquals(expected, actual);
    }
}
