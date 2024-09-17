package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjCategoryAsserts.*;
import static com.server.hkj.domain.HkjCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjCategoryMapperTest {

    private HkjCategoryMapper hkjCategoryMapper;

    @BeforeEach
    void setUp() {
        hkjCategoryMapper = new HkjCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjCategorySample1();
        var actual = hkjCategoryMapper.toEntity(hkjCategoryMapper.toDto(expected));
        assertHkjCategoryAllPropertiesEquals(expected, actual);
    }
}
