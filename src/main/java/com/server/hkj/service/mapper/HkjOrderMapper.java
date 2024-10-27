package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjCategory;
import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.domain.HkjOrder;
import com.server.hkj.domain.HkjProject;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.service.dto.HkjCategoryDTO;
import com.server.hkj.service.dto.HkjJewelryModelDTO;
import com.server.hkj.service.dto.HkjOrderDTO;
import com.server.hkj.service.dto.HkjProjectDTO;
import com.server.hkj.service.dto.UserExtraDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link HkjOrder} and its DTO {@link HkjOrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjOrderMapper extends EntityMapper<HkjOrderDTO, HkjOrder> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "userExtraId")
    @Mapping(target = "jewelry", source = "jewelry", qualifiedByName = "hkjJewelryModelId")
    @Mapping(target = "project", source = "project", qualifiedByName = "hkjProjectId")
    @Mapping(target = "category", source = "category", qualifiedByName = "hkjCategoryId")
    HkjOrderDTO toDto(HkjOrder s);

    @Named("userExtraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserExtraDTO toDtoUserExtraId(UserExtra userExtra);

    @Named("hkjJewelryModelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjJewelryModelDTO toDtoHkjJewelryModelId(HkjJewelryModel hkjJewelryModel);

    @Named("hkjProjectId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjProjectDTO toDtoHkjProjectId(HkjProject hkjProject);

    @Named("hkjCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    HkjCategoryDTO toDtoHkjCategoryId(HkjCategory hkjCategory);
}
