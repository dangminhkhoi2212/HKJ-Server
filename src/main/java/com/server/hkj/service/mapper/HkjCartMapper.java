package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjCart;
import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.service.dto.HkjCartDTO;
import com.server.hkj.service.dto.HkjJewelryModelDTO;
import com.server.hkj.service.dto.UserExtraDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link HkjCart} and its DTO {@link HkjCartDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjCartMapper extends EntityMapper<HkjCartDTO, HkjCart> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "userExtraId")
    @Mapping(target = "product", source = "product", qualifiedByName = "hkjJewelryModelId")
    HkjCartDTO toDto(HkjCart s);

    @Named("userExtraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserExtraDTO toDtoUserExtraId(UserExtra userExtra);

    @Named("hkjJewelryModelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "coverImage", source = "coverImage")
    @Mapping(target = "price", source = "price")
    HkjJewelryModelDTO toDtoHkjJewelryModelId(HkjJewelryModel hkjJewelryModel);
}
