package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjSalaryAsserts.*;
import static com.server.hkj.domain.HkjSalaryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjSalaryMapperTest {

    private HkjSalaryMapper hkjSalaryMapper;

    @BeforeEach
    void setUp() {
        hkjSalaryMapper = new HkjSalaryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjSalarySample1();
        var actual = hkjSalaryMapper.toEntity(hkjSalaryMapper.toDto(expected));
        assertHkjSalaryAllPropertiesEquals(expected, actual);
    }
}
