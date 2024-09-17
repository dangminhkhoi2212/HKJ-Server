package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjMaterialUsageAsserts.*;
import static com.server.hkj.domain.HkjMaterialUsageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjMaterialUsageMapperTest {

    private HkjMaterialUsageMapper hkjMaterialUsageMapper;

    @BeforeEach
    void setUp() {
        hkjMaterialUsageMapper = new HkjMaterialUsageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjMaterialUsageSample1();
        var actual = hkjMaterialUsageMapper.toEntity(hkjMaterialUsageMapper.toDto(expected));
        assertHkjMaterialUsageAllPropertiesEquals(expected, actual);
    }
}
