package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjEmployee;
import com.server.hkj.domain.HkjHire;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.service.dto.HkjEmployeeDTO;
import com.server.hkj.service.dto.HkjHireDTO;
import com.server.hkj.service.dto.UserExtraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjEmployee} and its DTO {@link HkjEmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjEmployeeMapper extends EntityMapper<HkjEmployeeDTO, HkjEmployee> {
    @Mapping(target = "userExtra", source = "userExtra", qualifiedByName = "userExtraId")
    @Mapping(target = "hkjHire", source = "hkjHire", qualifiedByName = "hkjHireId")
    HkjEmployeeDTO toDto(HkjEmployee s);

    @Named("userExtraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserExtraDTO toDtoUserExtraId(UserExtra userExtra);

    @Named("hkjHireId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjHireDTO toDtoHkjHireId(HkjHire hkjHire);
}
