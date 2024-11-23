package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.domain.HkjMaterial;
import com.server.hkj.domain.HkjMaterialUsage;
import com.server.hkj.domain.HkjTask;
import com.server.hkj.service.dto.HkjJewelryModelDTO;
import com.server.hkj.service.dto.HkjMaterialDTO;
import com.server.hkj.service.dto.HkjMaterialUsageDTO;
import com.server.hkj.service.dto.HkjTaskDTO;
import org.mapstruct.*;

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
