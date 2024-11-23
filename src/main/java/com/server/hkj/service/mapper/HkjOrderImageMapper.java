package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjOrderImage;
import com.server.hkj.domain.HkjOrderItem;
import com.server.hkj.service.dto.HkjOrderImageDTO;
import com.server.hkj.service.dto.HkjOrderItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjOrderImage} and its DTO {@link HkjOrderImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjOrderImageMapper extends EntityMapper<HkjOrderImageDTO, HkjOrderImage> {
    @Mapping(target = "orderItem", source = "orderItem", qualifiedByName = "hkjOrderItemId")
    HkjOrderImageDTO toDto(HkjOrderImage s);

    @Named("hkjOrderItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HkjOrderItemDTO toDtoHkjOrderItemId(HkjOrderItem hkjOrderItem);
}
