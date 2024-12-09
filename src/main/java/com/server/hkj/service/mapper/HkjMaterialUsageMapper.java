package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.domain.HkjMaterial;
import com.server.hkj.domain.HkjMaterialUsage;
import com.server.hkj.domain.HkjTask;
import com.server.hkj.service.dto.HkjJewelryModelDTO;
import com.server.hkj.service.dto.HkjMaterialDTO;
import com.server.hkj.service.dto.HkjMaterialUsageDTO;
import com.server.hkj.service.dto.HkjTaskDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link HkjMaterialUsage} and its DTO {@link HkjMaterialUsageDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjMaterialUsageMapper extends EntityMapper<HkjMaterialUsageDTO, HkjMaterialUsage> {
    @Mapping(target = "material", source = "material", qualifiedByName = "hkjMaterialId")
    @Mapping(target = "jewelry", source = "jewelry", qualifiedByName = "hkjJewelryModelId")
    @Mapping(target = "task", source = "task", qualifiedByName = "hkjTaskId")
    HkjMaterialUsageDTO toDto(HkjMaterialUsage s);

    @Named("hkjMaterialId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "unit", source = "unit")
    @Mapping(target = "pricePerUnit", source = "pricePerUnit")
    HkjMaterialDTO toDtoHkjMaterialId(HkjMaterial hkjMaterial);

    @Named("hkjJewelryModelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjJewelryModelDTO toDtoHkjJewelryModelId(HkjJewelryModel hkjJewelryModel);

    @Named("hkjTaskId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjTaskDTO toDtoHkjTaskId(HkjTask hkjTask);
}
