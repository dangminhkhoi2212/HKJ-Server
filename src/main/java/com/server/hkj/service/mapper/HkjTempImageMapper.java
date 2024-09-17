package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjTempImage;
import com.server.hkj.service.dto.HkjTempImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjTempImage} and its DTO {@link HkjTempImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjTempImageMapper extends EntityMapper<HkjTempImageDTO, HkjTempImage> {}
