package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjEmployee;
import com.server.hkj.domain.HkjSalary;
import com.server.hkj.service.dto.HkjEmployeeDTO;
import com.server.hkj.service.dto.HkjSalaryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjSalary} and its DTO {@link HkjSalaryDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjSalaryMapper extends EntityMapper<HkjSalaryDTO, HkjSalary> {
    @Mapping(target = "hkjEmployee", source = "hkjEmployee", qualifiedByName = "hkjEmployeeId")
    HkjSalaryDTO toDto(HkjSalary s);

    @Named("hkjEmployeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjEmployeeDTO toDtoHkjEmployeeId(HkjEmployee hkjEmployee);
}
