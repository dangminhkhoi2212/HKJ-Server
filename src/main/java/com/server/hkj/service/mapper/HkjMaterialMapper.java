package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjMaterial;
import com.server.hkj.domain.HkjMaterialImage;
import com.server.hkj.service.dto.HkjMaterialDTO;
import com.server.hkj.service.dto.HkjMaterialImageDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link HkjMaterial} and its DTO {@link HkjMaterialDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjMaterialMapper extends EntityMapper<HkjMaterialDTO, HkjMaterial> {
    @Mapping(target = "images", source = "images", qualifiedByName = "hkjMaterialImageId")
    HkjMaterialDTO toDto(HkjMaterial m);

    @Named("hkjMaterialImageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "url", source = "url")
    HkjMaterialImageDTO toDtoHkjJewelryModelId(HkjMaterialImage hkjMaterialImage);
}
