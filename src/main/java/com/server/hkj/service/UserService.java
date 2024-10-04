package com.server.hkj.service;

import com.server.hkj.config.Constants;
import com.server.hkj.domain.Authority;
import com.server.hkj.domain.User;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.repository.AuthorityRepository;
import com.server.hkj.repository.UserExtraRepository;
import com.server.hkj.repository.UserRepository;
import com.server.hkj.security.AuthoritiesConstants;
import com.server.hkj.service.dto.AdminUserDTO;
import com.server.hkj.service.dto.UserDTO;
import com.server.hkj.service.dto.UserExtraDTO;
import com.server.hkj.service.mapper.UserExtraMapper;
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
    private final UserExtraMapper userExtraMapper;

    public UserService(
        UserRepository userRepository,
        AuthorityRepository authorityRepository,
        CacheManager cacheManager,
        UserExtraRepository userExtraRepository,
        UserExtraService userExtraService,
        UserExtraMapper userExtraMapper
    ) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.userExtraRepository = userExtraRepository;
        this.cacheManager = cacheManager;
        this.userMapper = new UserMapper();
        this.userExtraService = userExtraService;
        this.userExtraMapper = userExtraMapper;
    }

    public void updateUser(User user) {
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isPresent()) {
            User newUser = userMapper.partialUpdate(existingUser.get(), user);
            userRepository.save(newUser);
            log.debug("Changed Information for User: {}", newUser);
            clearUserCaches(newUser);
            clearUserCaches(existingUser.get());
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
    public UserExtraDTO updateUser(
        String login,
        String firstName,
        String lastName,
        String email,
        String langKey,
        String imageUrl,
        String phoneNumber,
        String addressString,
        Set<Authority> authorities
    ) {
        User user = userRepository.findOneByLogin(login).orElseThrow(() -> new RuntimeException("No user found with login: " + login));

        user.setFirstName(firstName);
        user.setLastName(lastName);
        if (email != null) {
            user.setEmail(email.toLowerCase());
        }
        user.setLangKey(langKey);
        user.setImageUrl(imageUrl);
        user.setAuthorities(authorities);
        userRepository.save(user);
        clearUserCaches(user);

        Optional<UserExtra> userExtraOptional = userExtraRepository.findOneByUserLogin(login);
        UserExtra userExtra;
        if (userExtraOptional.isPresent()) {
            userExtra = userExtraOptional.get();
        } else {
            userExtra = new UserExtra();
            userExtra.setUser(user);
        }

        userExtra = userExtraService.setDetails(userExtra, phoneNumber, addressString);
        userExtraRepository.save(userExtra);
        log.debug("Changed Information for User Extra: {}", userExtra);
        return userExtraMapper.toDto(userExtra);
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

    private UserExtra syncUserWithIdP(Map<String, Object> details, User user) {
        // Save authorities to sync user roles/groups between IdP and JHipster's local database
        Set<String> dbAuthoritiesSet = new HashSet<>(getAuthorities());
        user
            .getAuthorities()
            .stream()
            .map(Authority::getName)
            .filter(authority -> !dbAuthoritiesSet.contains(authority) && AuthoritiesConstants.AUTHORITIES.contains(authority))
            .forEach(authority -> {
                log.debug("Saving authority '{}' in local database", authority);
                Authority authorityToSave = new Authority();
                authorityToSave.setName(authority);
                authorityRepository.save(authorityToSave);
            });

        Optional<User> existingUser = userRepository.findOneByLogin(user.getLogin());
        if (existingUser.isPresent()) {
            if (details.get("updated_at") != null) {
                Instant dbModifiedDate = existingUser.get().getLastModifiedDate();
                Instant idpModifiedDate = details.get("updated_at") instanceof Instant
                    ? (Instant) details.get("updated_at")
                    : Instant.ofEpochSecond((Integer) details.get("updated_at"));
                if (idpModifiedDate.isAfter(dbModifiedDate)) {
                    log.debug("Updating user '{}' in local database", user.getLogin());
                    updateUser(
                        user.getLogin(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getLangKey(),
                        user.getImageUrl(),
                        getDetailString(details, "phone_number"),
                        getDetailString(details, "address_string"),
                        user.getAuthorities()
                    );
                }
            } else {
                log.debug("Updating user '{}' in local database without timestamp check", user.getLogin());
                updateUser(
                    user.getLogin(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getLangKey(),
                    user.getImageUrl(),
                    getDetailString(details, "phone_number"),
                    getDetailString(details, "address_string"),
                    user.getAuthorities()
                );
            }
            return userExtraRepository
                .findOneByUserLogin(user.getLogin())
                .orElseThrow(() -> new RuntimeException("UserExtra not found after update"));
        } else {
            log.debug("Saving new user '{}' in local database", user.getLogin());
            userRepository.save(user);
            UserExtra userExtra = new UserExtra();
            userExtra.setUser(user);
            userExtra.setPhone(getDetailString(details, "phone_number"));
            userExtra.setAddress(getDetailString(details, "address_string"));
            userExtraRepository.save(userExtra);
            clearUserCaches(user);
            return userExtra;
        }
    }

    private String getDetailString(Map<String, Object> details, String key) {
        return details.get(key) != null ? details.get(key).toString() : "";
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
        Map<String, Object> attributes;
        if (authToken instanceof OAuth2AuthenticationToken) {
            attributes = ((OAuth2AuthenticationToken) authToken).getPrincipal().getAttributes();
        } else if (authToken instanceof JwtAuthenticationToken) {
            attributes = ((JwtAuthenticationToken) authToken).getTokenAttributes();
        } else {
            throw new IllegalArgumentException("AuthenticationToken is not OAuth2 or JWT!");
        }

        UserExtra userExtra = getUser(attributes);
        User user = userExtra.getUser();

        Set<Authority> authorities = authToken
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .filter(AuthoritiesConstants.AUTHORITIES::contains)
            .map(authority -> {
                Authority auth = new Authority();
                auth.setName(authority);
                return auth;
            })
            .collect(Collectors.toSet());

        user.setAuthorities(authorities);
        User savedUser = userRepository.save(user);
        UserExtra syncedUserExtra = syncUserWithIdP(attributes, savedUser);
        return new AdminUserDTO(syncedUserExtra);
    }

    private static UserExtra getUser(Map<String, Object> details) {
        User user = new User();
        UserExtra userExtra = new UserExtra();

        Boolean activated = Boolean.TRUE;
        String sub = String.valueOf(details.get("sub"));
        String username = details.get("preferred_username") != null ? ((String) details.get("preferred_username")).toLowerCase() : null;

        // Handle resource server JWT, where sub claim is email and uid is ID
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

        // Set name fields
        if (details.get("given_name") != null) {
            user.setFirstName((String) details.get("given_name"));
        } else if (details.get("name") != null) {
            user.setFirstName((String) details.get("name"));
        }

        if (details.get("family_name") != null) {
            user.setLastName((String) details.get("family_name"));
        }

        // Set email and activation status
        if (details.get("email_verified") != null) {
            activated = (Boolean) details.get("email_verified");
        }

        if (details.get("email") != null) {
            user.setEmail(((String) details.get("email")).toLowerCase());
        } else if (sub.contains("|") && (username != null && username.contains("@"))) {
            // Special handling for Auth0
            user.setEmail(username);
        } else {
            user.setEmail(sub);
        }

        // Set language
        if (details.get("langKey") != null) {
            user.setLangKey((String) details.get("langKey"));
        } else if (details.get("locale") != null) {
            String locale = (String) details.get("locale");
            if (locale.contains("_")) {
                locale = locale.substring(0, locale.indexOf('_'));
            } else if (locale.contains("-")) {
                locale = locale.substring(0, locale.indexOf('-'));
            }
            user.setLangKey(locale.toLowerCase());
        } else {
            user.setLangKey(Constants.DEFAULT_LANGUAGE);
        }

        if (details.get("picture") != null) {
            user.setImageUrl((String) details.get("picture"));
        }

        user.setActivated(activated);

        // Set UserExtra fields
        userExtra.setUser(user);
        userExtra.setPhone(details.get("phone_number") != null ? details.get("phone_number").toString() : "");
        userExtra.setAddress(details.get("address_string") != null ? details.get("address_string").toString() : "");

        return userExtra;
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }
}
