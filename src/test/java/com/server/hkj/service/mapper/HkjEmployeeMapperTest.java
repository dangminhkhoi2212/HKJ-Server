package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjEmployeeAsserts.*;
import static com.server.hkj.domain.HkjEmployeeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjEmployeeMapperTest {

    private HkjEmployeeMapper hkjEmployeeMapper;

    @BeforeEach
    void setUp() {
        hkjEmployeeMapper = new HkjEmployeeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjEmployeeSample1();
        var actual = hkjEmployeeMapper.toEntity(hkjEmployeeMapper.toDto(expected));
        assertHkjEmployeeAllPropertiesEquals(expected, actual);
    }
}
