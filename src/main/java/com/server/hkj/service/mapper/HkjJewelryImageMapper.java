package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjEmployee;
import com.server.hkj.domain.HkjJewelryImage;
import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.service.dto.HkjEmployeeDTO;
import com.server.hkj.service.dto.HkjJewelryImageDTO;
import com.server.hkj.service.dto.HkjJewelryModelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjJewelryImage} and its DTO {@link HkjJewelryImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjJewelryImageMapper extends EntityMapper<HkjJewelryImageDTO, HkjJewelryImage> {
    @Mapping(target = "uploadedBy", source = "uploadedBy", qualifiedByName = "hkjEmployeeId")
    @Mapping(target = "jewelryModel", source = "jewelryModel", qualifiedByName = "hkjJewelryModelId")
    HkjJewelryImageDTO toDto(HkjJewelryImage s);

    @Named("hkjEmployeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjEmployeeDTO toDtoHkjEmployeeId(HkjEmployee hkjEmployee);

    @Named("hkjJewelryModelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjJewelryModelDTO toDtoHkjJewelryModelId(HkjJewelryModel hkjJewelryModel);
}
