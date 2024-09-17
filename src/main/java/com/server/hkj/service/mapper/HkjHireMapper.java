package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjHire;
import com.server.hkj.domain.HkjPosition;
import com.server.hkj.service.dto.HkjHireDTO;
import com.server.hkj.service.dto.HkjPositionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjHire} and its DTO {@link HkjHireDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjHireMapper extends EntityMapper<HkjHireDTO, HkjHire> {
    @Mapping(target = "position", source = "position", qualifiedByName = "hkjPositionId")
    HkjHireDTO toDto(HkjHire s);

    @Named("hkjPositionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjPositionDTO toDtoHkjPositionId(HkjPosition hkjPosition);
}
