package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjCategory;
import com.server.hkj.domain.HkjTemplate;
import com.server.hkj.service.dto.HkjCategoryDTO;
import com.server.hkj.service.dto.HkjTemplateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjTemplate} and its DTO {@link HkjTemplateDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjTemplateMapper extends EntityMapper<HkjTemplateDTO, HkjTemplate> {
    @Mapping(target = "category", source = "category", qualifiedByName = "hkjCategoryId")
    HkjTemplateDTO toDto(HkjTemplate s);

    @Named("hkjCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjCategoryDTO toDtoHkjCategoryId(HkjCategory hkjCategory);
}
