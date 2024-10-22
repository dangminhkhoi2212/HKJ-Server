package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjHire;
import com.server.hkj.domain.HkjPosition;
import com.server.hkj.domain.User;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.service.dto.HkjHireDTO;
import com.server.hkj.service.dto.HkjPositionDTO;
import com.server.hkj.service.dto.UserDTO;
import com.server.hkj.service.dto.UserExtraDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link HkjHire} and its DTO {@link HkjHireDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjHireMapper extends EntityMapper<HkjHireDTO, HkjHire> {
    @Mapping(target = "position", source = "position", qualifiedByName = "hkjPositionId")
    @Mapping(target = "employee", source = "employee", qualifiedByName = "userExtraId")
    HkjHireDTO toDto(HkjHire s);

    @Named("hkjPositionId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    HkjPositionDTO toDtoHkjPositionId(HkjPosition hkjPosition);

    @Named("userExtraId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    UserExtraDTO toDtoUserExtraId(UserExtra userExtra);

    @Named("userId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    UserDTO toDtoUserId(User user);
}
