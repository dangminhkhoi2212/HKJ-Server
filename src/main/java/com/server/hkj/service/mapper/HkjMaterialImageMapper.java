package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjMaterial;
import com.server.hkj.domain.HkjMaterialImage;
import com.server.hkj.service.dto.HkjMaterialDTO;
import com.server.hkj.service.dto.HkjMaterialImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjMaterialImage} and its DTO {@link HkjMaterialImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjMaterialImageMapper extends EntityMapper<HkjMaterialImageDTO, HkjMaterialImage> {
    @Mapping(target = "material", source = "material", qualifiedByName = "hkjMaterialId")
    HkjMaterialImageDTO toDto(HkjMaterialImage s);

    @Named("hkjMaterialId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjMaterialDTO toDtoHkjMaterialId(HkjMaterial hkjMaterial);
}
