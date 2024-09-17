package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjJewelryImageAsserts.*;
import static com.server.hkj.domain.HkjJewelryImageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjJewelryImageMapperTest {

    private HkjJewelryImageMapper hkjJewelryImageMapper;

    @BeforeEach
    void setUp() {
        hkjJewelryImageMapper = new HkjJewelryImageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjJewelryImageSample1();
        var actual = hkjJewelryImageMapper.toEntity(hkjJewelryImageMapper.toDto(expected));
        assertHkjJewelryImageAllPropertiesEquals(expected, actual);
    }
}
