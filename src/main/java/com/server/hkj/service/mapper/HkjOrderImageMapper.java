package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjOrder;
import com.server.hkj.domain.HkjOrderImage;
import com.server.hkj.service.dto.HkjOrderDTO;
import com.server.hkj.service.dto.HkjOrderImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjOrderImage} and its DTO {@link HkjOrderImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjOrderImageMapper extends EntityMapper<HkjOrderImageDTO, HkjOrderImage> {
    @Mapping(target = "order", source = "order", qualifiedByName = "hkjOrderId")
    HkjOrderImageDTO toDto(HkjOrderImage s);

    @Named("hkjOrderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjOrderDTO toDtoHkjOrderId(HkjOrder hkjOrder);
}
