package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjJewelryModelAsserts.*;
import static com.server.hkj.domain.HkjJewelryModelTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjJewelryModelMapperTest {

    private HkjJewelryModelMapper hkjJewelryModelMapper;

    @BeforeEach
    void setUp() {
        hkjJewelryModelMapper = new HkjJewelryModelMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjJewelryModelSample1();
        var actual = hkjJewelryModelMapper.toEntity(hkjJewelryModelMapper.toDto(expected));
        assertHkjJewelryModelAllPropertiesEquals(expected, actual);
    }
}
