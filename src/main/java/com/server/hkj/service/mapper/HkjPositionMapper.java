package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjPosition;
import com.server.hkj.service.dto.HkjPositionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjPosition} and its DTO {@link HkjPositionDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjPositionMapper extends EntityMapper<HkjPositionDTO, HkjPosition> {}
