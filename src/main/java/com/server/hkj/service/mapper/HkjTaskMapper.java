package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjProject;
import com.server.hkj.domain.HkjTask;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.service.dto.HkjProjectDTO;
import com.server.hkj.service.dto.HkjTaskDTO;
import com.server.hkj.service.dto.UserExtraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjTask} and its DTO {@link HkjTaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjTaskMapper extends EntityMapper<HkjTaskDTO, HkjTask> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "userExtraId")
    @Mapping(target = "hkjProject", source = "hkjProject", qualifiedByName = "hkjProjectId")
    HkjTaskDTO toDto(HkjTask s);

    @Named("userExtraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserExtraDTO toDtoUserExtraId(UserExtra userExtra);

    @Named("hkjProjectId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjProjectDTO toDtoHkjProjectId(HkjProject hkjProject);
}
