package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjHireAsserts.*;
import static com.server.hkj.domain.HkjHireTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjHireMapperTest {

    private HkjHireMapper hkjHireMapper;

    @BeforeEach
    void setUp() {
        hkjHireMapper = new HkjHireMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjHireSample1();
        var actual = hkjHireMapper.toEntity(hkjHireMapper.toDto(expected));
        assertHkjHireAllPropertiesEquals(expected, actual);
    }
}
