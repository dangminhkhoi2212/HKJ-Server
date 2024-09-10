package com.server.hkj.web.rest;

import com.server.hkj.service.UserExtraService;
import com.server.hkj.service.UserService;
import com.server.hkj.service.dto.AdminUserDTO;
import com.server.hkj.service.dto.UserDTO;
import com.server.hkj.service.dto.response.ResponseCT;
import com.server.hkj.service.dto.response.ResponseCTBuilder;
import com.server.hkj.service.dto.response.ResponseErrorCT;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api")
@Slf4j
public class PublicUserResource {

    private final UserService userService;
    private final UserExtraService userExtraService;

    public PublicUserResource(UserService userService, UserExtraService userExtraService) {
        this.userService = userService;
        this.userExtraService = userExtraService;
    }

    /**
     * {@code GET /users} : get all users with only public information - calling this method is allowed for anyone.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllPublicUsers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get all public User names");

        final Page<UserDTO> page = userService.getAllPublicUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/users/role")
    public ResponseCT<List<AdminUserDTO>> getAllPublicUsersByRole(Pageable pageable, @Param(value = "role") String role) {
        try {
            final Page<AdminUserDTO> page = userExtraService.getUsersByRole(pageable, role);
            return new ResponseCTBuilder<List<AdminUserDTO>>().addData(page.getContent()).build();
        } catch (Exception e) {
            return new ResponseCTBuilder<List<AdminUserDTO>>().error(ResponseErrorCT.builder().detail(e.getMessage()).build()).build();
        }
    }
}
