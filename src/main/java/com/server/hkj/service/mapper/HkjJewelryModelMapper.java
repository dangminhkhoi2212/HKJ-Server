package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.domain.HkjProject;
import com.server.hkj.service.dto.HkjJewelryModelDTO;
import com.server.hkj.service.dto.HkjProjectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjJewelryModel} and its DTO {@link HkjJewelryModelDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjJewelryModelMapper extends EntityMapper<HkjJewelryModelDTO, HkjJewelryModel> {
    @Mapping(target = "project", source = "project", qualifiedByName = "hkjProjectId")
    HkjJewelryModelDTO toDto(HkjJewelryModel s);

    @Named("hkjProjectId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjProjectDTO toDtoHkjProjectId(HkjProject hkjProject);
}
