package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjCategory;
import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.domain.HkjMaterial;
import com.server.hkj.domain.HkjOrder;
import com.server.hkj.domain.HkjOrderItem;
import com.server.hkj.service.dto.HkjCategoryDTO;
import com.server.hkj.service.dto.HkjJewelryModelDTO;
import com.server.hkj.service.dto.HkjMaterialDTO;
import com.server.hkj.service.dto.HkjOrderDTO;
import com.server.hkj.service.dto.HkjOrderItemDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link HkjOrderItem} and its DTO {@link HkjOrderItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjOrderItemMapper extends EntityMapper<HkjOrderItemDTO, HkjOrderItem> {
    @Mapping(target = "material", source = "material", qualifiedByName = "hkjMaterialId")
    @Mapping(target = "order", source = "order", qualifiedByName = "hkjOrderId")
    @Mapping(target = "product", source = "product", qualifiedByName = "hkjJewelryModelId")
    @Mapping(target = "category", source = "category", qualifiedByName = "hkjCategoryId")
    HkjOrderItemDTO toDto(HkjOrderItem s);

    @Named("hkjMaterialId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjMaterialDTO toDtoHkjMaterialId(HkjMaterial hkjMaterial);

    @Named("hkjOrderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjOrderDTO toDtoHkjOrderId(HkjOrder hkjOrder);

    @Named("hkjJewelryModelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "coverImage", source = "coverImage")
    @Mapping(target = "price", source = "price")
    HkjJewelryModelDTO toDtoHkjJewelryModelId(HkjJewelryModel hkjJewelryModel);

    @Named("hkjCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    HkjCategoryDTO toDtoHkjCategoryId(HkjCategory hkjCategory);
}
