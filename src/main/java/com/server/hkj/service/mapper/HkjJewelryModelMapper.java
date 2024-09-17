package com.server.hkj.service.mapper;

import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.service.dto.HkjJewelryModelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HkjJewelryModel} and its DTO {@link HkjJewelryModelDTO}.
 */
@Mapper(componentModel = "spring")
public interface HkjJewelryModelMapper extends EntityMapper<HkjJewelryModelDTO, HkjJewelryModel> {}
