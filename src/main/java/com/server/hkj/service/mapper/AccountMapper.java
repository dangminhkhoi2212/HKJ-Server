package com.server.hkj.service.mapper;

import com.server.hkj.domain.Authority;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.service.dto.AccountDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper for the entity {@link UserExtra} and its DTO {@link AccountDTO}.
 */
@Mapper(componentModel = "spring")
public interface AccountMapper extends EntityMapper<AccountDTO, UserExtra> {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "login")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.imageUrl", target = "imageUrl")
    @Mapping(source = "user.activated", target = "activated")
    @Mapping(source = "user.langKey", target = "langKey")
    @Mapping(source = "user.createdBy", target = "createdBy")
    @Mapping(source = "user.createdDate", target = "createdDate")
    @Mapping(source = "user.lastModifiedBy", target = "lastModifiedBy")
    @Mapping(source = "user.lastModifiedDate", target = "lastModifiedDate")
    @Mapping(source = "user.authorities", target = "authorities", qualifiedByName = "mapAuthorities")
    AccountDTO toDto(UserExtra userExtra);

    @Named("mapAuthorities")
    default Set<String> mapAuthorities(Set<Authority> authorities) {
        return authorities.stream().map(Authority::getName).collect(Collectors.toSet());
    }

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget UserExtra entity, AccountDTO dto);
}
