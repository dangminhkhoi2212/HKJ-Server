package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjEmployee;
import com.server.hkj.domain.HkjProject;
import com.server.hkj.domain.HkjTemplate;
import com.server.hkj.service.dto.HkjEmployeeDTO;
import com.server.hkj.service.dto.HkjProjectDTO;
import com.server.hkj.service.dto.HkjTemplateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjProject} and its DTO {@link HkjProjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjProjectMapper extends EntityMapper<HkjProjectDTO, HkjProject> {
    @Mapping(target = "template", source = "template", qualifiedByName = "hkjTemplateId")
    @Mapping(target = "manager", source = "manager", qualifiedByName = "hkjEmployeeId")
    HkjProjectDTO toDto(HkjProject s);

    @Named("hkjTemplateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjTemplateDTO toDtoHkjTemplateId(HkjTemplate hkjTemplate);

    @Named("hkjEmployeeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjEmployeeDTO toDtoHkjEmployeeId(HkjEmployee hkjEmployee);
}
