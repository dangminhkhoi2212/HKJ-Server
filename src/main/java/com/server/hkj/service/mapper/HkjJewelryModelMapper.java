package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjCategory;
import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.domain.HkjMaterial;
import com.server.hkj.domain.HkjMaterialUsage;
import com.server.hkj.domain.HkjProject;
import com.server.hkj.service.dto.HkjCategoryDTO;
import com.server.hkj.service.dto.HkjJewelryModelDTO;
import com.server.hkj.service.dto.HkjMaterialDTO;
import com.server.hkj.service.dto.HkjMaterialUsageDTO;
import com.server.hkj.service.dto.HkjProjectDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link HkjJewelryModel} and its DTO {@link HkjJewelryModelDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjJewelryModelMapper extends EntityMapper<HkjJewelryModelDTO, HkjJewelryModel> {
    @Mapping(target = "category", source = "category", qualifiedByName = "hkjCategoryId")
    @Mapping(target = "project", source = "project", qualifiedByName = "hkjProjectId")
    HkjJewelryModelDTO toDto(HkjJewelryModel s);

    @Named("hkjCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    HkjCategoryDTO toDtoHkjCategoryId(HkjCategory hkjCategory);

    @Named("hkjProjectId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    HkjProjectDTO toDtoHkjProjectId(HkjProject hkjProject);

    @Named("hkjMaterialId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    HkjMaterialDTO toDtoHkjMaterialId(HkjMaterial hkjMaterial);

    @Named("hkjMaterialUsageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "usage", source = "usage")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "material", source = "material")
    HkjMaterialUsageDTO toDtoHkjMaterialUsageId(HkjMaterialUsage hkjMaterialUsage);
}
