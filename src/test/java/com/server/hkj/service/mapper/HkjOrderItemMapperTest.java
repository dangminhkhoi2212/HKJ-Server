package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjOrderItemAsserts.*;
import static com.server.hkj.domain.HkjOrderItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjOrderItemMapperTest {

    private HkjOrderItemMapper hkjOrderItemMapper;

    @BeforeEach
    void setUp() {
        hkjOrderItemMapper = new HkjOrderItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjOrderItemSample1();
        var actual = hkjOrderItemMapper.toEntity(hkjOrderItemMapper.toDto(expected));
        assertHkjOrderItemAllPropertiesEquals(expected, actual);
    }
}
