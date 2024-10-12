package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.domain.HkjTrackSearchImage;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.service.dto.HkjJewelryModelDTO;
import com.server.hkj.service.dto.HkjTrackSearchImageDTO;
import com.server.hkj.service.dto.UserExtraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjTrackSearchImage} and its DTO {@link HkjTrackSearchImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjTrackSearchImageMapper extends EntityMapper<HkjTrackSearchImageDTO, HkjTrackSearchImage> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userExtraId")
    @Mapping(target = "jewelry", source = "jewelry", qualifiedByName = "hkjJewelryModelId")
    HkjTrackSearchImageDTO toDto(HkjTrackSearchImage s);

    @Named("userExtraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserExtraDTO toDtoUserExtraId(UserExtra userExtra);

    @Named("hkjJewelryModelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjJewelryModelDTO toDtoHkjJewelryModelId(HkjJewelryModel hkjJewelryModel);
}
