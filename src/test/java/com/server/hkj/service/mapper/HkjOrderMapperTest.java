package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjOrderAsserts.*;
import static com.server.hkj.domain.HkjOrderTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjOrderMapperTest {

    private HkjOrderMapper hkjOrderMapper;

    @BeforeEach
    void setUp() {
        hkjOrderMapper = new HkjOrderMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjOrderSample1();
        var actual = hkjOrderMapper.toEntity(hkjOrderMapper.toDto(expected));
        assertHkjOrderAllPropertiesEquals(expected, actual);
    }
}
