package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjSalary;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.service.dto.HkjSalaryDTO;
import com.server.hkj.service.dto.UserExtraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjSalary} and its DTO {@link HkjSalaryDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjSalaryMapper extends EntityMapper<HkjSalaryDTO, HkjSalary> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "userExtraId")
    HkjSalaryDTO toDto(HkjSalary s);

    @Named("userExtraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserExtraDTO toDtoUserExtraId(UserExtra userExtra);
}
