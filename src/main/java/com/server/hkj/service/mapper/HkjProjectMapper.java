package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjCategory;
import com.server.hkj.domain.HkjProject;
import com.server.hkj.domain.User;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.service.dto.HkjCategoryDTO;
import com.server.hkj.service.dto.HkjProjectDTO;
import com.server.hkj.service.dto.UserDTO;
import com.server.hkj.service.dto.UserExtraDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link HkjProject} and its DTO {@link HkjProjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjProjectMapper extends EntityMapper<HkjProjectDTO, HkjProject> {
    @Mapping(target = "manager", source = "manager", qualifiedByName = "userExtraId")
    @Mapping(target = "category", source = "category", qualifiedByName = "hkjCategoryId")
    HkjProjectDTO toDto(HkjProject s);

    @Named("userExtraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    UserExtraDTO toDtoUserExtraId(UserExtra userExtra);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    UserDTO toDtoUserId(User user);

    @Named("hkjCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    HkjCategoryDTO toDtoHkjCategoryId(HkjCategory hkjCategory);
}
