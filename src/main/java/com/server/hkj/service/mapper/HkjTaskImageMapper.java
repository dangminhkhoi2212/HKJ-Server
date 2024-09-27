package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjTask;
import com.server.hkj.domain.HkjTaskImage;
import com.server.hkj.service.dto.HkjTaskDTO;
import com.server.hkj.service.dto.HkjTaskImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjTaskImage} and its DTO {@link HkjTaskImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjTaskImageMapper extends EntityMapper<HkjTaskImageDTO, HkjTaskImage> {
    @Mapping(target = "task", source = "task", qualifiedByName = "hkjTaskId")
    HkjTaskImageDTO toDto(HkjTaskImage s);

    @Named("hkjTaskId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjTaskDTO toDtoHkjTaskId(HkjTask hkjTask);
}
