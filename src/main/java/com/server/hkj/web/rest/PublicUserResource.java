package com.server.hkj.web.rest;

import com.server.hkj.service.UserExtraService;
import com.server.hkj.service.UserService;
import com.server.hkj.service.dto.AccountDTO;
import com.server.hkj.service.dto.AdminUserDTO;
import com.server.hkj.service.dto.UserDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<List<AccountDTO>> getAllPublicUsersByRole(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @Param(value = "role") String role
    ) {
        final Page<AccountDTO> page = userExtraService.getUsersByRole(pageable, role);
        return new ResponseEntity<List<AccountDTO>>(page.getContent(), HttpStatus.OK);
    }

    @GetMapping("/users/role/count")
    public ResponseEntity<Long> getAllPublicUsersByRoleCount(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @Param(value = "role") String role
    ) {
        final Page<AccountDTO> page = userExtraService.getUsersByRole(pageable, role);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return new ResponseEntity<Long>(Long.valueOf(page.getNumberOfElements()), headers, HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<AdminUserDTO> putMethodName(@PathVariable String id, @RequestBody AdminUserDTO user) {
        //TODO: process PUT request
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), null);
        return new ResponseEntity<AdminUserDTO>(null, headers, HttpStatus.OK);
    }
}
