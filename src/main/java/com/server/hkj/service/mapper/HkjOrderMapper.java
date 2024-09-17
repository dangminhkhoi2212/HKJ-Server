package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.domain.HkjOrder;
import com.server.hkj.domain.HkjProject;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.service.dto.HkjJewelryModelDTO;
import com.server.hkj.service.dto.HkjOrderDTO;
import com.server.hkj.service.dto.HkjProjectDTO;
import com.server.hkj.service.dto.UserExtraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjOrder} and its DTO {@link HkjOrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjOrderMapper extends EntityMapper<HkjOrderDTO, HkjOrder> {
    @Mapping(target = "project", source = "project", qualifiedByName = "hkjProjectId")
    @Mapping(target = "customOrder", source = "customOrder", qualifiedByName = "hkjJewelryModelId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "userExtraId")
    HkjOrderDTO toDto(HkjOrder s);

    @Named("hkjProjectId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjProjectDTO toDtoHkjProjectId(HkjProject hkjProject);

    @Named("hkjJewelryModelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjJewelryModelDTO toDtoHkjJewelryModelId(HkjJewelryModel hkjJewelryModel);

    @Named("userExtraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserExtraDTO toDtoUserExtraId(UserExtra userExtra);
}
