package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjEmployee;
import com.server.hkj.domain.HkjProject;
import com.server.hkj.domain.HkjTask;
import com.server.hkj.domain.HkjTemplateStep;
import com.server.hkj.service.dto.HkjEmployeeDTO;
import com.server.hkj.service.dto.HkjProjectDTO;
import com.server.hkj.service.dto.HkjTaskDTO;
import com.server.hkj.service.dto.HkjTemplateStepDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjTask} and its DTO {@link HkjTaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjTaskMapper extends EntityMapper<HkjTaskDTO, HkjTask> {
    @Mapping(target = "templateStep", source = "templateStep", qualifiedByName = "hkjTemplateStepId")
    @Mapping(target = "employee", source = "employee", qualifiedByName = "hkjEmployeeId")
    @Mapping(target = "hkjProject", source = "hkjProject", qualifiedByName = "hkjProjectId")
    HkjTaskDTO toDto(HkjTask s);

    @Named("hkjTemplateStepId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjTemplateStepDTO toDtoHkjTemplateStepId(HkjTemplateStep hkjTemplateStep);

    @Named("hkjEmployeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjEmployeeDTO toDtoHkjEmployeeId(HkjEmployee hkjEmployee);

    @Named("hkjProjectId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjProjectDTO toDtoHkjProjectId(HkjProject hkjProject);
}
