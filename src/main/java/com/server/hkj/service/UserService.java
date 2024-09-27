package com.server.hkj.service;

import com.server.hkj.config.Constants;
import com.server.hkj.domain.Authority;
import com.server.hkj.domain.User;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.repository.AuthorityRepository;
import com.server.hkj.repository.UserExtraRepository;
import com.server.hkj.repository.UserRepository;
import com.server.hkj.security.AuthoritiesConstants;
import com.server.hkj.security.SecurityUtils;
import com.server.hkj.service.dto.AdminUserDTO;
import com.server.hkj.service.dto.UserDTO;
import com.server.hkj.service.mapper.UserMapper;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing users.
 */
@Service
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final UserExtraRepository userExtraRepository;

    private final CacheManager cacheManager;

    private final UserMapper userMapper;

    private final UserExtraService userExtraService;

    public UserService(
        UserRepository userRepository,
        AuthorityRepository authorityRepository,
        CacheManager cacheManager,
        UserExtraRepository userExtraRepository,
        UserExtraService userExtraService
    ) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.userExtraRepository = userExtraRepository;
        this.cacheManager = cacheManager;
        this.userMapper = new UserMapper();
        this.userExtraService = userExtraService;
    }

    public void updateUser(User user) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent()) {
            User newUser = userMapper.partialUpdate(existingUser.get(), user);
            userRepository.save(newUser);
            log.info("Changed Information for User: {}", newUser);
            this.clearUserCaches(newUser);
            this.clearUserCaches(existingUser.get());
        }
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(
        String firstName,
        String lastName,
        String email,
        String langKey,
        String imageUrl,
        String phoneNumber,
        String addressString
    ) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email != null ? email.toLowerCase() : null);
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                userRepository.save(user);

                UserExtra userExtra = userExtraRepository
                    .findOneByUserLogin(user.getLogin())
                    .orElseGet(() -> {
                        UserExtra newUserExtra = new UserExtra();
                        newUserExtra.setUser(user);
                        return newUserExtra;
                    });

                userExtra = userExtraService.setDetails(userExtra, phoneNumber, addressString);
                userExtraRepository.save(userExtra);

                clearUserCaches(user);
            });
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).toList();
    }

    private String getDetailString(Map<String, Object> details, String key) {
        return details.get(key) != null ? details.get(key).toString() : "";
    }

    private void saveAuthority(String authorityName) {
        log.info("Saving authority '{}' in local database", authorityName);
        Authority authority = new Authority();
        authority.setName(authorityName);
        authorityRepository.save(authority);
    }

    private UserExtra syncUserWithIdP(Map<String, Object> details, User user) {
        // save authorities in to sync user roles/groups between IdP and JHipster's local database
        Set<String> dbAuthorities = new HashSet<>(getAuthorities());
        user
            .getAuthorities()
            .stream()
            .map(Authority::getName)
            .filter(authority -> !dbAuthorities.contains(authority) && AuthoritiesConstants.AUTHORITIES.contains(authority))
            .forEach(this::saveAuthority);

        // save account in to sync users between IdP and JHipster's local database
        return userRepository
            .findOneByLogin(user.getLogin())
            .map(existingUser -> updateExistingUser(existingUser, user, details))
            .orElseGet(() -> createNewUser(user, details));
    }

    private UserExtra createNewUser(User user, Map<String, Object> details) {
        log.info("Saving user '{}' in local database", user.getLogin());
        User newUser = userRepository.save(user);
        UserExtra userExtra = new UserExtra();
        userExtra.setUser(newUser);
        userExtra.setPhone(getDetailString(details, "phone_number"));
        userExtra.setAddress(getDetailString(details, "address_string"));
        userExtraRepository.save(userExtra);
        clearUserCaches(user);
        clearUserCaches(newUser);
        return userExtra;
    }

    private UserExtra updateExistingUser(User existingUser, User user, Map<String, Object> details) {
        log.info("Updating user '{}' in local database", user.getLogin());
        //update authorities
        existingUser.setAuthorities(user.getAuthorities());
        userRepository.save(existingUser);
        updateUser(
            existingUser.getFirstName(),
            existingUser.getLastName(),
            existingUser.getEmail(),
            existingUser.getLangKey(),
            existingUser.getImageUrl(),
            getDetailString(details, "phone_number"),
            getDetailString(details, "address_string")
        );
        return userExtraRepository.findOneByUserLogin(existingUser.getLogin()).get();
    }

    private Map<String, Object> getAttributes(AbstractAuthenticationToken authToken) {
        if (authToken instanceof OAuth2AuthenticationToken) {
            return ((OAuth2AuthenticationToken) authToken).getPrincipal().getAttributes();
        } else if (authToken instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) authToken).getTokenAttributes();
        }
        throw new IllegalArgumentException("AuthenticationToken is not OAuth2 or JWT!");
    }

    private boolean shouldUpdate(User existingUser, Map<String, Object> details) {
        if (details.get("updated_at") == null) return true;
        Instant dbModifiedDate = existingUser.getLastModifiedDate();
        Instant idpModifiedDate = details.get("updated_at") instanceof Instant
            ? (Instant) details.get("updated_at")
            : Instant.ofEpochSecond((Integer) details.get("updated_at"));
        return idpModifiedDate.isAfter(dbModifiedDate);
    }

    private Set<Authority> getAuthoritiesFromToken(AbstractAuthenticationToken authToken) {
        return authToken
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .filter(AuthoritiesConstants.AUTHORITIES::contains)
            .map(authorityName -> {
                Authority auth = new Authority();
                auth.setName(authorityName);
                return auth;
            })
            .collect(Collectors.toSet());
    }

    /**
     * Returns the user from an OAuth 2.0 login or resource server with JWT.
     * Synchronizes the user in the local repository.
     *
     * @param authToken the authentication token.
     * @return the user from the authentication.
     */

    @Transactional
    public AdminUserDTO getUserFromAuthentication(AbstractAuthenticationToken authToken) {
        Map<String, Object> attributes = getAttributes(authToken);
        UserExtra userExtra = getUser(attributes);
        User user = userExtra.getUser();
        user.setAuthorities(getAuthoritiesFromToken(authToken));
        UserExtra userExtraSync = syncUserWithIdP(attributes, user);
        return new AdminUserDTO(userExtraSync != null ? userExtraSync : userExtra);
    }

    private static UserExtra getUser(Map<String, Object> details) {
        User user = new User();
        UserExtra userExtra = new UserExtra();
        Boolean activated = Boolean.TRUE;
        String sub = String.valueOf(details.get("sub"));
        String username = null;
        if (details.get("preferred_username") != null) {
            username = ((String) details.get("preferred_username")).toLowerCase();
        }
        // handle resource server JWT, where sub claim is email and uid is ID
        if (details.get("uid") != null) {
            user.setId((String) details.get("uid"));
            user.setLogin(sub);
        } else {
            user.setId(sub);
        }
        if (username != null) {
            user.setLogin(username);
        } else if (user.getLogin() == null) {
            user.setLogin(user.getId());
        }
        if (details.get("given_name") != null) {
            user.setFirstName((String) details.get("given_name"));
        } else if (details.get("name") != null) {
            user.setFirstName((String) details.get("name"));
        }
        if (details.get("family_name") != null) {
            user.setLastName((String) details.get("family_name"));
        }

        if (details.get("email_verified") != null) {
            activated = (Boolean) details.get("email_verified");
        }
        if (details.get("email") != null) {
            user.setEmail(((String) details.get("email")).toLowerCase());
        } else if (sub.contains("|") && (username != null && username.contains("@"))) {
            // special handling for Auth0
            user.setEmail(username);
        } else {
            user.setEmail(sub);
        }
        if (details.get("langKey") != null) {
            user.setLangKey((String) details.get("langKey"));
        } else if (details.get("locale") != null) {
            // trim off country code if it exists
            String locale = (String) details.get("locale");
            if (locale.contains("_")) {
                locale = locale.substring(0, locale.indexOf('_'));
            } else if (locale.contains("-")) {
                locale = locale.substring(0, locale.indexOf('-'));
            }
            user.setLangKey(locale.toLowerCase());
        } else {
            // set langKey to default if not specified by IdP
            user.setLangKey(Constants.DEFAULT_LANGUAGE);
        }
        if (details.get("picture") != null) {
            user.setImageUrl((String) details.get("picture"));
        }
        user.setActivated(activated);

        if (details.get("phone_number") != null) {
            userExtra.setPhone(details.get("phone_number").toString());
        }
        if (details.get("address_string") != null) {
            userExtra.setAddress(details.get("address_string").toString());
        }
        userExtra.setUser(user);
        return userExtra;
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }
}
