package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjJewelryImage;
import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.service.dto.HkjJewelryImageDTO;
import com.server.hkj.service.dto.HkjJewelryModelDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link HkjJewelryImage} and its DTO {@link HkjJewelryImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjJewelryImageMapper extends EntityMapper<HkjJewelryImageDTO, HkjJewelryImage> {
    @Mapping(target = "jewelryModel", source = "jewelryModel", qualifiedByName = "hkjJewelryModelId")
    HkjJewelryImageDTO toDto(HkjJewelryImage s);

    @Named("hkjJewelryModelId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "category", ignore = true)
    HkjJewelryModelDTO toDtoHkjJewelryModelId(HkjJewelryModel hkjJewelryModel);
}
