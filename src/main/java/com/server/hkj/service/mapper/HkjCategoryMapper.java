package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjCategory;
import com.server.hkj.service.dto.HkjCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjCategory} and its DTO {@link HkjCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjCategoryMapper extends EntityMapper<HkjCategoryDTO, HkjCategory> {}
