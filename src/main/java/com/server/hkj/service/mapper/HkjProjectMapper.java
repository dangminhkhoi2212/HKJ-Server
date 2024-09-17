package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjCategory;
import com.server.hkj.domain.HkjEmployee;
import com.server.hkj.domain.HkjProject;
import com.server.hkj.service.dto.HkjCategoryDTO;
import com.server.hkj.service.dto.HkjEmployeeDTO;
import com.server.hkj.service.dto.HkjProjectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjProject} and its DTO {@link HkjProjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjProjectMapper extends EntityMapper<HkjProjectDTO, HkjProject> {
    @Mapping(target = "category", source = "category", qualifiedByName = "hkjCategoryId")
    @Mapping(target = "manager", source = "manager", qualifiedByName = "hkjEmployeeId")
    HkjProjectDTO toDto(HkjProject s);

    @Named("hkjCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjCategoryDTO toDtoHkjCategoryId(HkjCategory hkjCategory);

    @Named("hkjEmployeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjEmployeeDTO toDtoHkjEmployeeId(HkjEmployee hkjEmployee);
}
