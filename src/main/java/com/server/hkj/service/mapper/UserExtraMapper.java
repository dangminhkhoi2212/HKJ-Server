package com.server.hkj.service.mapper;

import com.server.hkj.domain.User;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.service.dto.UserDTO;
import com.server.hkj.service.dto.UserExtraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserExtra} and its DTO {@link UserExtraDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserExtraMapper extends EntityMapper<UserExtraDTO, UserExtra> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    UserExtraDTO toDto(UserExtra s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}