package com.server.hkj.service.mapper;

import com.server.hkj.domain.UserExtra;
import com.server.hkj.service.dto.AdminUserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminUserMapper extends EntityMapper<AdminUserDTO, UserExtra> {}
