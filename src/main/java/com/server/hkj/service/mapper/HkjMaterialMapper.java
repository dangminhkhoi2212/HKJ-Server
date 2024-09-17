package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjMaterial;
import com.server.hkj.service.dto.HkjMaterialDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjMaterial} and its DTO {@link HkjMaterialDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjMaterialMapper extends EntityMapper<HkjMaterialDTO, HkjMaterial> {}
