package com.server.hkj.service.mapper;

import static com.server.hkj.domain.HkjTrackSearchImageAsserts.*;
import static com.server.hkj.domain.HkjTrackSearchImageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HkjTrackSearchImageMapperTest {

    private HkjTrackSearchImageMapper hkjTrackSearchImageMapper;

    @BeforeEach
    void setUp() {
        hkjTrackSearchImageMapper = new HkjTrackSearchImageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getHkjTrackSearchImageSample1();
        var actual = hkjTrackSearchImageMapper.toEntity(hkjTrackSearchImageMapper.toDto(expected));
        assertHkjTrackSearchImageAllPropertiesEquals(expected, actual);
    }
}
