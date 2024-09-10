package com.server.hkj.web.rest;

import com.server.hkj.domain.UserExtra;
import com.server.hkj.service.UserExtraService;
import com.server.hkj.service.UserService;
import com.server.hkj.service.dto.AdminUserDTO;
import com.server.hkj.service.dto.UserExtraDTO;
import com.server.hkj.service.dto.response.ResponseCT;
import com.server.hkj.service.dto.response.ResponseCTBuilder;
import com.server.hkj.service.dto.response.ResponseErrorCT;
import com.server.hkj.service.mapper.UserExtraMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        private AccountResourceException(String message) {
            super(message);
        }
    }

    static final Logger log = LoggerFactory.getLogger(AccountResource.class);

    UserService userService;
    UserExtraService userExtraService;
    UserExtraMapper userExtraMapper;

    public AccountResource(UserService userService, UserExtraService userExtraService, UserExtraMapper userExtraMapper) {
        this.userExtraMapper = userExtraMapper;
        this.userService = userService;
        this.userExtraService = userExtraService;
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @param principal the current user; resolves to {@code null} if not authenticated.
     * @return the current user.
     * @throws AccountResourceException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public AdminUserDTO getAccount(Principal principal) {
        log.info("REST request to get current user : {}", principal.toString());
        if (principal instanceof AbstractAuthenticationToken) {
            return userService.getUserFromAuthentication((AbstractAuthenticationToken) principal);
        } else {
            throw new AccountResourceException("User could not be found");
        }
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }
    // @GetMapping("/accounts")
    // public ResponseCT<List<AdminUserDTO>> getAccounts(Pageable pageable) {
    //     try {
    //         List<UserExtraDTO> users = userExtraService.findAll(pageable).getContent();
    //         List<UserExtra> userExtras = userExtraMapper.toEntity(users);
    //         return new ResponseCTBuilder<List<AdminUserDTO>>().addData(adminUserMapper.toDto(userExtras)).build();
    //     } catch (Exception e) {
    //         return new ResponseCTBuilder<List<AdminUserDTO>>()
    //             .error(new ResponseErrorCT("500", "Internal Server Error", e.getMessage()))
    //             .build();
    //     }
    // }
}
