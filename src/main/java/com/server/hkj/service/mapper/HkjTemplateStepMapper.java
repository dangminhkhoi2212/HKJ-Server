package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjTemplate;
import com.server.hkj.domain.HkjTemplateStep;
import com.server.hkj.service.dto.HkjTemplateDTO;
import com.server.hkj.service.dto.HkjTemplateStepDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjTemplateStep} and its DTO {@link HkjTemplateStepDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjTemplateStepMapper extends EntityMapper<HkjTemplateStepDTO, HkjTemplateStep> {
    @Mapping(target = "hkjTemplate", source = "hkjTemplate", qualifiedByName = "hkjTemplateId")
    HkjTemplateStepDTO toDto(HkjTemplateStep s);

    @Named("hkjTemplateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjTemplateDTO toDtoHkjTemplateId(HkjTemplate hkjTemplate);
}
