package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjCartAsserts.*;
import static com.server.hkj.domain.HkjCartTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjCartMapperTest {

    private HkjCartMapper hkjCartMapper;

    @BeforeEach
    void setUp() {
        hkjCartMapper = new HkjCartMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjCartSample1();
        var actual = hkjCartMapper.toEntity(hkjCartMapper.toDto(expected));
        assertHkjCartAllPropertiesEquals(expected, actual);
    }
}
