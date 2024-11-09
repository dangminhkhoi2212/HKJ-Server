package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjCart;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.service.dto.HkjCartDTO;
import com.server.hkj.service.dto.UserExtraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjCart} and its DTO {@link HkjCartDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjCartMapper extends EntityMapper<HkjCartDTO, HkjCart> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "userExtraId")
    HkjCartDTO toDto(HkjCart s);

    @Named("userExtraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserExtraDTO toDtoUserExtraId(UserExtra userExtra);
}
